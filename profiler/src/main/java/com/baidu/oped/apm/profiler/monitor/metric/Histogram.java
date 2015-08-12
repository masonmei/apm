
package com.baidu.oped.apm.profiler.monitor.metric;

import com.baidu.oped.apm.bootstrap.context.Metric;

/**
 * class Histogram 
 *
 * @author meidongxu@baidu.com
 */
public interface Histogram extends Metric {
    short getServiceType();

    void addResponseTime(int millis);

    HistogramSnapshot createSnapshot();
}
