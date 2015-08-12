
package com.baidu.oped.apm.collector.util;

import java.lang.management.ManagementFactory;

/**
 * class CollectorUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class CollectorUtils {

    private CollectorUtils() {
    }

    public static String getServerIdentifier() {

        // if the return value is not unique, it will be changed to MAC address or IP address.
        // It means that the return value has format of "pid@hostname" (it is possible to be duplicate for "localhost")
        return ManagementFactory.getRuntimeMXBean().getName();
    }

}
