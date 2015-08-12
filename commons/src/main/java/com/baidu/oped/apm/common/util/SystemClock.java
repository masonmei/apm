
package com.baidu.oped.apm.common.util;

/**
 * class SystemClock 
 *
 * @author meidongxu@baidu.com
 */
public final class SystemClock implements Clock {

    public static final Clock INSTANCE = new SystemClock();

    private SystemClock() {
    }

    @Override
    public final long getTime() {
        return System.currentTimeMillis();
    }
}
