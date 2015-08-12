
package com.baidu.oped.apm.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class DateUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class DateUtils {

    private static final ThreadLocal<DateFormat> CACHE = new ThreadLocal<DateFormat>() {
        @SuppressWarnings("unused")
        private final String name = DateUtils.class.getName();

        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(FORMAT);
        }
    };

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss SSS";

    private DateUtils() {
    }

    public static String longToDateStr(long date) {
        final DateFormat dateFormat = CACHE.get();
        return dateFormat.format(date);
    }

    public static String longToDateStr(long date, String fmt) {
        String pattern = (fmt == null) ? FORMAT : fmt;
        final SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(date));
    }
}
