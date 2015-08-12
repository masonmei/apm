
package com.baidu.oped.apm.profiler.monitor.codahale;

import com.codahale.metrics.Meter;
import com.baidu.oped.apm.profiler.monitor.EventRateMonitor;

/**
 * class MetricEventRateMonitor 
 *
 * @author meidongxu@baidu.com
 */
public class MetricEventRateMonitor implements EventRateMonitor {

    final Meter delegate;

    public MetricEventRateMonitor(Meter delegate) {
        if (delegate == null) {
            throw new NullPointerException("Meter delegate is null");
        }
        this.delegate = delegate;
    }

    public void event() {
        this.delegate.mark();
    }

    public void events(long count) {
        this.delegate.mark(count);
    }

    public long getCount() {
        return this.delegate.getCount();
    }

    public double getRate() {
        return this.delegate.getMeanRate();
    }

    public Meter getDelegate() {
        return this.delegate;
    }

    public String toString() {
        return "MetricEventRateMonitor(delegate=" + this.delegate + ")";
    }

}
