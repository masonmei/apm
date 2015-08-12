package com.baidu.oped.apm.bootstrap.util;

/**
 * class TimeObject
 *
 * @author meidongxu@baidu.com
 */
public class TimeObject {
    private long cancelTime;
    private long sendTime;

    public void markCancelTime() {
        cancelTime = System.currentTimeMillis();
    }

    public long getCancelTime() {
        return cancelTime;
    }

    public void markSendTime() {
        this.sendTime = System.currentTimeMillis();
    }

    public long getSendTime() {
        return System.currentTimeMillis() - this.sendTime;
    }
}
