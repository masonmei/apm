
package com.baidu.oped.apm.profiler.monitor;

/**
 * class EventRateMonitor 
 *
 * @author meidongxu@baidu.com
 */
public interface EventRateMonitor {

    void event();

    void events(final long count);

    long getCount();

    double getRate();

}
