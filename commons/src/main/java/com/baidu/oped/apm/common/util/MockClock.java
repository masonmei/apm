
package com.baidu.oped.apm.common.util;

/**
 * class MockClock 
 *
 * @author meidongxu@baidu.com
 */
public class MockClock implements Clock {

    private long time;

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public long getTime() {
        return time;
    }
}
