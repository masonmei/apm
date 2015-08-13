
package com.baidu.oped.apm.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String formatTime(long timeMillis) {
        Date now = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(now);
    }

    public static String formatTime(long timeMillis, String formatStr) {
        Date now = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(now);
    }

    public static long getTimeStampL(String timeStr) throws Exception {
        return getTimeStampL(timeStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static long getTimeStampL(String timeStr, String format) throws Exception {
        DateFormat dateformat = new SimpleDateFormat(format);
        Date date = dateformat.parse(timeStr);
        return date.getTime();
    }

    public static int getTimeStampI(String timeStr) throws Exception {
        return getTimeStampI(timeStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static int getTimeStampI(String timeStr, String format) throws Exception {
        return (int) (getTimeStampL(timeStr, format) / 1000);
    }

    public static String getDay(long timestamp) {
        return formatTime(timestamp, "yyyyMMdd");
    }

}
