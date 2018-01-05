package com.reed.timeout.event;

public class TimeoutNotifyEvent<T> extends AbstractTimeoutEvent<T> {
    /**
     * 
     */
    private static final long serialVersionUID = 4060677779187441612L;
    /**到期通知次数 */
    private int notifyCount;
    /**任务实际结束时间*/
    private long endTime = System.currentTimeMillis();

    public TimeoutNotifyEvent() {
        super();
    }

    public TimeoutNotifyEvent(String topic, String key, TimeoutTypeEnum type, long startTime,
            long duration, T t) {
        super(topic, key, type, startTime, duration, t);
    }

    public TimeoutNotifyEvent(String topic, String key, TimeoutTypeEnum type, T t) {
        super(topic, key, type, t);
    }

    public int getNotifyCount() {
        return notifyCount;
    }

    public void setNotifyCount(int notifyCount) {
        this.notifyCount = notifyCount;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

}
