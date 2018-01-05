package com.reed.timeout.event;

/**
 * 计时删除事件
 *
 * @param <T>
 */
public class TimeoutClearEvent<T> extends AbstractTimeoutEvent<T> {

    /**
     * 
     */
    private static final long serialVersionUID = 2725023014587912361L;

    public TimeoutClearEvent() {
        super();
    }

    public TimeoutClearEvent(String topic, String key, TimeoutTypeEnum type, T t) {
        super(topic, key, type, t);
    }

}
