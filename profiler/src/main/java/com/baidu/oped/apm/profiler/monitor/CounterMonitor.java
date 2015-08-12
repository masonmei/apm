
package com.baidu.oped.apm.profiler.monitor;

/**
 * class CounterMonitor 
 *
 * @author meidongxu@baidu.com
 */
public interface CounterMonitor {

    void incr();

    void incr(final long delta);

    void decr();

    void decr(final long delta);

    void reset();

    long getCount();

}
