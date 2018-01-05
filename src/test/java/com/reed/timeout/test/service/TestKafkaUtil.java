package com.reed.timeout.test.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.reed.common.util.JsonUtil;
import com.reed.timeout.event.AbstractTimeoutEvent;
import com.reed.timeout.event.TimeoutNotifyEvent;
import com.reed.timeout.event.TimeoutStartEvent;
import com.reed.timeout.event.TimeoutTypeEnum;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class TestKafkaUtil {

    private static String topic_in = "timeout-events-in";
    private static String topic_out = "timeout-events-out";
    private static String broker_list = "localhost:9092";
    private static String zk_list = "localhost:2181";

    private static KafkaProducer<String, byte[]> producer = null;

    private static ConsumerConnector consumer = null;

    static {
        // 生产者配置文件，具体配置可参考ProducerConfig类源码，或者参考官网介绍
        Map<String, Object> config = new HashMap<String, Object>();
        // kafka服务器地址
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker_list);
        // kafka消息序列化类 即将传入对象序列化为字节数组
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.ByteArraySerializer");
        // kafka消息key序列化类 若传入key的值，则根据该key的值进行hash散列计算出在哪个partition上
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024 * 1024 * 5);
        // 往kafka服务器提交消息间隔时间，0则立即提交不等待
        config.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        // 消费者配置文件
        Properties props = new Properties();
        // zookeeper地址
        props.put("zookeeper.connect", zk_list);
        // 组id
        props.put("group.id", "test");
        // 自动提交消费情况间隔时间
        props.put("auto.commit.interval.ms", "1000");

        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        producer = new KafkaProducer<String, byte[]>(config);
        consumer = Consumer.createJavaConsumerConnector(consumerConfig);
    }

    /**
     *启动一个消费程序 
    * @param topic 要消费的topic名称
    * @param handler 自己的处理逻辑的实现
    * @param threadCount 消费线程数，该值应小于等于partition个数，多了也没用
     */
    public static void startConsumer(String topic,
            final MqMessageHandler<AbstractTimeoutEvent<Object>> handler, int threadCount)
            throws Exception {
        if (threadCount < 1)
            throw new Exception("处理消息线程数最少为1");
        // 设置处理消息线程数，线程数应小于等于partition数量，若线程数大于partition数量，则多余的线程则闲置，不会进行工作
        // key:topic名称 value:线程数
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(threadCount));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap =
                consumer.createMessageStreams(topicCountMap);
        // 声明一个线程池，用于消费各个partition
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        // 获取对应topic的消息队列
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        // 为每一个partition分配一个线程去消费
        for (final KafkaStream stream : streams) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ConsumerIterator<byte[], byte[]> it = stream.iterator();
                    // 有信息则消费，无信息将会阻塞
                    while (it.hasNext()) {
                        AbstractTimeoutEvent message = null;
                        try {
                            // 将字节码反序列化成相应的对象
                            byte[] bytes = it.next().message();
                            message = JsonUtil.getObjectMapper().readValue(bytes,
                                    AbstractTimeoutEvent.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        // 调用自己的业务逻辑
                        try {
                            handler.handle(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    /**
     *发送消息
     */
    public static Future<RecordMetadata> send(String topic, AbstractTimeoutEvent value)
            throws Exception {
        try {
            // 将对象序列化称字节码
            byte[] bytes = JsonUtil.getObjectMapper().writeValueAsBytes(value);
            Future<RecordMetadata> future =
                    producer.send(new ProducerRecord<String, byte[]>(topic, value.getKey(),
                            bytes));
            return future;
        } catch (Exception e) {
            throw e;
        }
    }

    // 内部抽象类 用于实现自己的处理逻辑
    public static abstract class MqMessageHandler<BaseEvent> {
        public abstract void handle(BaseEvent message);
    }

    public static void main(String[] args) throws Exception {
        // 发送信息
        int size = 50000;
        long duration = 300;
        Object ss = new Object();
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            int index = r.nextInt(3) + 1;
            TimeoutStartEvent<Object> e = new TimeoutStartEvent<>(topic_in,
                    String.valueOf(i),
                    TimeoutTypeEnum.values()[index - 1],
                    System.currentTimeMillis(), duration * index,
                    ss);
            send(topic_in, e);
            Thread.sleep(30);
        }
        // 启动消费者，启动后每次有消息则打印对象信息
        TestKafkaUtil.startConsumer(topic_out,
                new MqMessageHandler<AbstractTimeoutEvent<Object>>() {
                    @Override
                    public void handle(AbstractTimeoutEvent e) {
                        // 实现自己的处理逻辑，这里只打印出消息
                        if (e != null) {

                            Object obj =
                                    JsonUtil.getObjectMapper().convertValue(e,
                                            AbstractTimeoutEvent.class);
                            if (obj instanceof TimeoutNotifyEvent) {
                                System.out.println(obj.toString());
                            }

                        }
                    }
                }, 2);
    }
}
