
package com.baidu.oped.apm.profiler.monitor.metric;

/**
 * class AcceptHistogram 
 *
 * @author meidongxu@baidu.com
 */
public interface AcceptHistogram {
    boolean addResponseTime(String parentApplicationName, short serviceType, int millis);
}
