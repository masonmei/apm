
package com.baidu.oped.apm.profiler.monitor;

/**
 * class HistogramMonitor 
 *
 * @author meidongxu@baidu.com
 */
public interface HistogramMonitor {

    void reset();

    void update(final long value);

    long getCount();

}
