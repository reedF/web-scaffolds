package com.reed.timeout.event;

/**
 * 超时类型
 *
 */
public enum TimeoutTypeEnum {
    AGENT("坐席超时", 1), VISITOR("访客超时", 2), ROBOT("机器人超时", 3),SILENCE("不活跃会话超时",4);
    /** 超时类型名称*/
    private String name;
    /** 类型取值*/
    private int index;

    // 构造方法
    private TimeoutTypeEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (TimeoutTypeEnum c : TimeoutTypeEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
