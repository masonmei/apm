
package com.baidu.oped.apm.profiler.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * class SystemUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class SystemUtils {
    private static final ThreadMXBean THREAD_MX_BEAN = ManagementFactory.getThreadMXBean();

    private SystemUtils() {
    }

    public static long getCurrentThreadCpuTime() {
        return THREAD_MX_BEAN.getCurrentThreadCpuTime();
    }

    public static long getCurrentThreadUserTime() {
        return THREAD_MX_BEAN.getCurrentThreadUserTime();
    }
}
