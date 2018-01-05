package com.reed.timeout.pojo;

import java.io.Serializable;
import java.util.Objects;

import com.reed.timeout.service.DelayQueueService;

/**
 * 任务对象
 * 
 *
 */
public class TaskItem implements Runnable, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3515506873510772967L;

    private String key;
    /** 开始时间*/
    private long start;
    /** 有效时长，单位：秒*/
    private long duration;
    /** kafka partion id */
    private int kafkaPartionId;

    private DelayQueueService delayQueueService;

    public TaskItem() {

    }

    public TaskItem(String key) {
        this.key = key;
    }

    public TaskItem(String key, long start, long duration, int kafkaPartionId) {
        super();
        this.key = key;
        this.start = start;
        this.duration = duration;
        this.kafkaPartionId = kafkaPartionId;
    }

    @Override
    public void run() {
        delayQueueService.sendNotifyEvent(this.key, this.kafkaPartionId);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getKafkaPartionId() {
        return kafkaPartionId;
    }

    public void setKafkaPartionId(int kafkaPartionId) {
        this.kafkaPartionId = kafkaPartionId;
    }

    public DelayQueueService getDelayQueueService() {
        return delayQueueService;
    }

    public void setDelayQueueService(DelayQueueService delayQueueService) {
        this.delayQueueService = delayQueueService;
    }

    @Override
    public int hashCode() {
        if (this.key == null) {
            return 0;
        }
        return this.key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (obj.getClass() != this.getClass()))
            return false;
        if (obj instanceof TaskItem) {
            return Objects.equals(this.key, ((TaskItem) obj).getKey());
        }
        return false;
    }

}
