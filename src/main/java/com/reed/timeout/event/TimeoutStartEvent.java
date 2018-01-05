package com.reed.timeout.event;
/**
 * 计时开始事件
 *
 * @param <T>
 */
public class TimeoutStartEvent<T> extends AbstractTimeoutEvent<T> {

    /**
     * 
     */
    private static final long serialVersionUID = 1628902632680054443L;
    /** 超时提醒次数*/
    private int remindCount = 1;

    public TimeoutStartEvent() {
        super();
    }

    public TimeoutStartEvent(String topic, String key, TimeoutTypeEnum type, long startTime,
            long duration, T t) {
        super(topic, key, type, startTime, duration, t);
    }

    public TimeoutStartEvent(String topic, String key, TimeoutTypeEnum type, T t) {
        super(topic, key, type, t);
    }

    public int getRemindCount() {
        return remindCount;
    }

    public void setRemindCount(int remindCount) {
        this.remindCount = remindCount;
    }

}
