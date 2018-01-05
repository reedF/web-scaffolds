package com.reed.timeout.watcher;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.reed.timeout.service.DelayQueueService;

import lombok.extern.slf4j.Slf4j;

/**
 * zookeeper Watcher to connect zk \ create node \ listen node change
 * 监听node，提供consumer的分布式HA部署（仅一个consumer生效，多个节点时自动容灾）
 * @author reed
 * 
 */
@Component
@Slf4j
public class ZkKafkaConsumerWatcher implements Watcher {

    /**
     * 同步工具
     * 
     * **/
    private CountDownLatch count = new CountDownLatch(1);

    /** 本地服务标志,判断从znode上取到的节点是否是本地节点 */
    @Value("${kafka.consumer.properties.client.id}")
    private String localTag;

    /** zookeeper 集群地址*/
    @Value("${kafka.consumer.properties.zookeeper.connect}")
    private String zkClaster;
    @Value("${kafka.consumer.properties.group.id}")
    private String group;

    /** zookeeper znode root */
    private final static String root = "/consumers/";
    /** consumer ids*/
    private final static String node = "/ids";

    /** zookeeper connect time out */
    private static int timeout = 5000;
    /**
     * zk实例
     * **/
    private ZooKeeper zk;
    

    //kafka consumer bean
    //@Autowired
    //private TimeoutEventKafkaConsumer consumer;

    @Autowired
    private DelayQueueService delayQueueService;

    public String getZkClaster() {
        return zkClaster;
    }

    public void setZkClaster(String zkClaster) {
        this.zkClaster = zkClaster;
    }

    @PostConstruct
    public void init() {
        try {
            zk = new ZooKeeper(zkClaster, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        count.countDown();
                    }

                }
            });
            count.await();

            startWatcher();
            log.info("===========ZK Kafka Consumer Watcher start=============");
        } catch (IOException | InterruptedException e) {
            log.error("zk watcher init failed=============>" + e.getMessage());
        }
    }

    @PreDestroy
    public void close() {
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                log.error("zk close failed:{}", e.getMessage());
            }
        }
        log.info("===========ZK Kafka Consumer Watcher close=============");
    }

    /**
     * 查询consumer子节点，并注册监听
     * 
     * @return
     */
    private List<String> getChildren() {
        List<String> list = null;
        try {
            // list consumer nodes and watch
            list = zk.getChildren(root + group + node, this);
            if (list == null || list.isEmpty()) {
                log.warn("No consumer node in group:{}", group);
            }
        } catch (InterruptedException | KeeperException e) {
            log.error("zk get children failed:{}", e.getMessage());
        }
        return list;
    }

    /**
     * 初始化监听节点
     * 当前不存在消费者时，才会新建消费者
     * 
     */
    private void startWatcher() {
        if (isRootExist()) {
            // add listen
            List<String> list = getChildren();
            // no consumer
            if (list == null || list.size() < 1) {
                startConsumer(true);
            }
        } else {
            // not exist
            startConsumer(true);
        }
    }

    /**
     * 检查是否已创建过consumer,并注册监听创建事件
     * @return
     */
    private boolean isRootExist() {
        boolean r = false;
        String path = root + group + node;
        try {
            Stat stat = zk.exists(path, this);
            if (stat != null) {
                r = true;
            }
        } catch (InterruptedException | KeeperException e) {
            log.error("zk check consumer group node exist failed,path is:{},error is:{}",
                    path,
                    e.getMessage());
        }
        return r;
    }

    /**
     * 处理消费者变更事件，同时再次注册监听
     */
    private void checkConsumerChange() {
        List<String> list = getChildren();
        if (list != null && list.size() > 0) {
            for (String s : list) {
                if (s != null) {
                    // local is active
                    s.contains(localTag);
                    return;
                }
            }
            // local is not active
            delayQueueService.clearTaskInQueue();
        } else {
            // no consumer
            startConsumer(true);
        }
    }

    /**
     * 启动消费者
     * @param reloadTask 是否重新加载任务列表
     */
    private void startConsumer(boolean reloadTask) {
        //reload task first then to start consumer
        if (reloadTask) {
            delayQueueService.reloadTask();
        }
        //init consumer
        //consumer.init();
    }

    /**
     * 监听事件处理
     */
    @Override
    public void process(WatchedEvent event) {
        if (event != null) {
            String path = event.getPath();
            // 如果发现，监听的节点变化，则重新进行监听
            try {
                if (event.getType() == Event.EventType.NodeCreated) {
                    log.debug("Consumer group node created........");
                    // 注册监听
                    getChildren();
                }
                if (event.getType() == Event.EventType.NodeChildrenChanged) {
                    log.debug("Consumer group node changed........");
                    checkConsumerChange();
                }
            } catch (Exception e) {
                log.error("zk watcher event failed=========>path:{},event:{},ex:{}",
                        event.getPath(), event.getType(), e.getMessage());
            }
        }
    }

}
