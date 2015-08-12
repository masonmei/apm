
package com.baidu.oped.apm.profiler.monitor;

/**
 * class MonitorRegistry 
 *
 * @author meidongxu@baidu.com
 */
public interface MonitorRegistry {

    HistogramMonitor newHistogramMonitor(final MonitorName monitorName);

    EventRateMonitor newEventRateMonitor(final MonitorName monitorName);

    CounterMonitor newCounterMonitor(final MonitorName monitorName);

}
