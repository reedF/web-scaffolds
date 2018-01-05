package com.reed.timeout.event;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.context.ApplicationEvent;

/**
 * 超时事件基础类
 *
 * @param <T>
 */
public abstract class AbstractTimeoutEvent<T> extends ApplicationEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 5997508683753910785L;
    /**kafka topic*/
    private String topic;
    /** event unique key*/
    private String key;
    /** 超时类型*/
    private TimeoutTypeEnum type;
    /**计时起始时间 */
    private long startTime = System.currentTimeMillis();
    /** 有效期，单位：秒*/
    private long duration;
    /** 业务实体对象*/
    private T businessObj;

    public AbstractTimeoutEvent() {
        super(new Object());
    }

    public AbstractTimeoutEvent(String topic, String key, TimeoutTypeEnum type, T t) {
        super(new Object());
        this.topic = topic;
        this.key = key;
        this.type = type;
        this.businessObj = t;
    }

    public AbstractTimeoutEvent(String topic, String key, TimeoutTypeEnum type, long startTime,
            long duration, T t) {
        super(new Object());
        this.topic = topic;
        this.key = key;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        this.businessObj = t;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public TimeoutTypeEnum getType() {
        return type;
    }

    public void setType(TimeoutTypeEnum type) {
        this.type = type;
    }

    public T getBusinessObj() {
        return businessObj;
    }

    public void setBusinessObj(T businessObj) {
        this.businessObj = businessObj;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
