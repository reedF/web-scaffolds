package com.reed.timeout.service;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.reed.common.util.JsonUtil;
import com.reed.timeout.event.AbstractTimeoutEvent;
import com.reed.timeout.event.TimeoutNotifyEvent;
import com.reed.timeout.event.TimeoutStartEvent;
import com.reed.timeout.pojo.Task;
import com.reed.timeout.pojo.TaskItem;

import lombok.extern.slf4j.Slf4j;

/**
 * delay queue service
 * @author reed
 *
 */
@Slf4j
@Service
@DependsOn("timeoutRedisTemplate")
public class DelayQueueService {

    public static final String DELAY_QUEUE_KEY_PREFIX = "timeout:delay:";
    public static final String DELAY_QUEUE_KEY_ITEM = DELAY_QUEUE_KEY_PREFIX + "task:%s:%s:%s";
    @Value("${kafka.topic}")
    private String eventTopic;
    @Value("${timeout.delay.pool.size}")
    private int threadPoolSize;
    /**
     * 创建一个最初为空的新 DelayQueue
     */
    private DelayQueue<Task<Runnable>> queue = new DelayQueue<>();
    /**
     * 线程池
     */
    private ExecutorService executor;
    /**
     * 守护线程
     */
    private Thread daemonThread;
    private static boolean running;

    //本地spring event生产者，发送超时到期通知事件，实际环境可使用kafa生产者代替
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    @Qualifier("timeoutRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 初始化守护线程
     */
    @PostConstruct
    public void init() {
        executor = Executors.newFixedThreadPool(threadPoolSize);
        running = true;
        daemonThread = new Thread(() -> execute());
        daemonThread.setDaemon(true);
        daemonThread.setName("Task Queue Daemon Thread");
        daemonThread.start();
        // reloadTask();
        log.info("===========Task Queue Daemon Thread start=============");
    }

    @PreDestroy
    public void destroy() {
        running = false;
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        clearTaskInQueue();
        log.info("===========Task Queue Daemon Thread stop=============");
    }

    /**
     * 监听任务队列
     */
    @SuppressWarnings("rawtypes")
    private void execute() {
        while (running) {
            try {
                // 从延迟队列中取值,如果没有对象过期则队列一直等待，
                Task t1 = queue.take();
                if (t1 != null) {
                    // 修改任务的状态
                    TaskItem task = (TaskItem) t1.getTaskItem();
                    if (task == null) {
                        continue;
                    }
                    executor.execute(task);
                    if (log.isDebugEnabled()) {
                        long end = System.currentTimeMillis();
                        long cost = (end - task.getStart()) / 1000 - task.getDuration();
                        log.debug(
                                "[Delay Queue monitor>>>>>>][Total:{},Task done:{}, start:{},end:{},deviation:{}]",
                                getTaskCount(), task.getKey(),
                                new Date(task.getStart()), new Date(end), cost);
                    }
                }
            } catch (InterruptedException e) {
                log.error("Delay Queue Thread error:{}", e.getMessage());
            }
        }
    }

    /**
     * 添加任务
     * @param event
     * @param kafkaPartionId
     */
    @SuppressWarnings("rawtypes")
    public void addTask(TimeoutStartEvent event, int kafkaPartionId) {
        if (event != null && event.getKey() != null && event.getType() != null
                && !checkTime(event)) {
            putTask(generateKey((AbstractTimeoutEvent) event, kafkaPartionId), event.getStartTime(),
                    event.getDuration(),
                    kafkaPartionId);
            addCache(event, kafkaPartionId);
        }
    }

    /**
     * 加入队列
     * @param key 任务唯一标识
     * @param start 起始时间
     * @param duration 有效期，单位：秒
     */
    private void putTask(String key, long start, long duration, int kafkaPartionId) {
        TaskItem task = new TaskItem(key, start, duration, kafkaPartionId);
        task.setDelayQueueService(this);
        // 转换成ns
        long nanoTime = TimeUnit.NANOSECONDS.convert(duration, TimeUnit.SECONDS);
        // 创建一个任务
        Task<Runnable> k = new Task<>(nanoTime, task);
        // 将任务放在延迟的队列中
        if (queue.contains(k)) {
            queue.remove(k);
        }
        queue.put(k);

    }

    /**
     * 删除任务
     * @param key
     * @param kafkaPartionId
     * @return
     */
    @SuppressWarnings("rawtypes")
    public boolean delTask(AbstractTimeoutEvent event, int kafkaPartionId) {
        boolean r = false;
        if (event != null && event.getType() != null && event.getKey() != null) {
            String key = generateKey(event, kafkaPartionId);
            Task<Runnable> task =
                    new Task<Runnable>(new TaskItem(key));
            r = queue.remove(task);
            delCache(key);
            r = true;
        }
        return r;
    }

    /**
     * 清除队列中全部任务，注：不清缓存
     */
    public void clearTaskInQueue() {
        if (queue != null && !queue.isEmpty()) {
            queue.clear();
        }
    }

    public int getTaskCount() {
        return queue.size();
    }

    /**
     * 任务到期通知
     * @param key
     * @param kafkaPartionId
     */
    @SuppressWarnings("rawtypes")
    public void sendNotifyEvent(String key, int kafkaPartionId) {
        if (key != null) {
            TimeoutNotifyEvent e = generateTimeoutNotifyEvent(key);
            if (e != null) {
                eventPublisher.publishEvent(e);
            }
            delCache(key);
        }
    }

    @SuppressWarnings("rawtypes")
    public void addCache(TimeoutStartEvent event, int kafkaPartionId) {
        if (event != null && event.getKey() != null) {
            String key = generateKey(event, kafkaPartionId);
            String value = JsonUtil.toJson(event);
            redisTemplate.boundValueOps(key).set(value);
        }
    }

    public void delCache(String key) {
        if (key != null) {
            if (redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
        }
    }

    /**
     * 检查是否已到期
     * @param event
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    private boolean checkTime(TimeoutStartEvent event) {
        boolean r = false;
        if (event != null) {
            long endTime = event.getStartTime() + event.getDuration() * 1000;
            long now = System.currentTimeMillis();
            // 已到期,不进队，直接发通知
            if (now > endTime) {
                r = true;
                TimeoutNotifyEvent notify = new TimeoutNotifyEvent<>(
                        this.eventTopic,
                        event.getKey(), event.getType(), event.getStartTime(),
                        event.getDuration(), event.getBusinessObj());
                eventPublisher.publishEvent(notify);
                log.debug("check time and push to kafka directly>>>>>>>>msg:{}", notify.toString());
            }
        }
        return r;
    }

    @SuppressWarnings("rawtypes")
    private TimeoutNotifyEvent generateTimeoutNotifyEvent(String key) {
        TimeoutNotifyEvent event = null;
        if (redisTemplate.hasKey(key)) {
            String msg = redisTemplate.boundValueOps(key).get();
            if (msg != null) {
                TimeoutStartEvent startEvent;
                try {
                    startEvent = (TimeoutStartEvent) JsonUtil.getObjectMapper()
                            .readValue(msg, TimeoutStartEvent.class);
                    if (startEvent != null) {
                        event = new TimeoutNotifyEvent<>(
                                this.eventTopic,
                                startEvent.getKey(), startEvent.getType(),
                                startEvent.getStartTime(),
                                startEvent.getDuration(), startEvent.getBusinessObj());
                    }
                } catch (IOException e) {
                    log.error("JSON Conver error:{}", e.getMessage());
                }
            }
        }

        return event;
    }

    /**
     * redis key and task key
     * @param event
     * @param kafkaPartionId
     * @return
     */
    @SuppressWarnings("rawtypes")
    private String generateKey(AbstractTimeoutEvent event, int kafkaPartionId) {
        String key = String.format(DELAY_QUEUE_KEY_ITEM, event.getType().getIndex(), kafkaPartionId,
                event.getKey());
        return key;
    }

    /**
     * reload task from redis
     */
    @SuppressWarnings({"rawtypes"})
    public void reloadTask() {
        Set<String> set = redisTemplate.keys(DELAY_QUEUE_KEY_PREFIX + "task:*");
        if (set != null && !set.isEmpty()) {
            int kafkaPartionId = 0;
            for (String key : set) {
                if (key != null) {
                    String[] s = key.split(":");
                    kafkaPartionId = Integer.parseInt(s[5]);
                    String obj = redisTemplate.boundValueOps(key).get();
                    try {
                        TimeoutStartEvent event =
                                JsonUtil.getObjectMapper().readValue(obj, TimeoutStartEvent.class);
                        if (event != null) {
                            // 未到期
                            if (!checkTime(event)) {
                                putTask(key, event.getStartTime(), event.getDuration(),
                                        kafkaPartionId);
                            } else {
                                // 已到期，删cache
                                delCache(key);
                            }
                        }
                    } catch (IOException e) {
                        log.error("JSON Conver error:{}", e.getMessage());
                    }
                }
            }
        }
    }

}
