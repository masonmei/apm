
package com.baidu.oped.apm.common.util;

/**
 * class TimeUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class TimeUtils {
    private TimeUtils() {
    }

    public static long reverseTimeMillis(long currentTimeMillis) {
        return Long.MAX_VALUE - currentTimeMillis;
    }

    public static long reverseCurrentTimeMillis() {
        return reverseTimeMillis(System.currentTimeMillis());
    }

    public static long recoveryTimeMillis(long reverseCurrentTimeMillis) {
        return Long.MAX_VALUE - reverseCurrentTimeMillis;
    }
}
