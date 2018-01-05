package com.reed.timeout.pojo;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *
 * 队列中要执行的任务
 *
 */
public class Task<T extends Runnable> implements Delayed {
    /**
     * 到期时间
     */
    private final long time;

    /**
     * 任务对象
     */
    private final T taskItem;

    public Task(T t) {
        this.time = System.nanoTime();
        this.taskItem = t;
    }

    public Task(long timeout, T t) {
        this.time = System.nanoTime() + timeout;
        this.taskItem = t;
    }

    /**
     * 返回与此对象相关的剩余延迟时间，以给定的时间单位表示
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.time - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed other) {

        if (other == this) // compare zero ONLY if same object
            return 0;
        if (other instanceof Task) {
            Task x = (Task) other;
            long diff = time - x.time;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
        }
        long d = getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS);
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    public T getTaskItem() {
        return this.taskItem;
    }

    @Override
    public int hashCode() {
        return taskItem.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (obj.getClass() != this.getClass()))
            return false;
        if (obj instanceof Task) {
            return this.taskItem.equals(((Task) obj).getTaskItem());
        }
        return false;
    }

}
