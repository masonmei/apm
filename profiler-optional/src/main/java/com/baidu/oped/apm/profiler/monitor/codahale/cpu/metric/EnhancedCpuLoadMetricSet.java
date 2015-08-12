
package com.baidu.oped.apm.profiler.monitor.codahale.cpu.metric;

import com.codahale.metrics.Gauge;
import com.baidu.oped.apm.profiler.monitor.codahale.cpu.metric.AbstractCpuLoadMetricSet;
import com.sun.management.OperatingSystemMXBean;

/**
 * class EnhancedCpuLoadMetricSet 
 *
 * @author meidongxu@baidu.com
 */
public class EnhancedCpuLoadMetricSet extends AbstractCpuLoadMetricSet {

    @Override
    protected Gauge<Double> getJvmCpuLoadGauge(final OperatingSystemMXBean operatingSystemMXBean) {
        return new Gauge<Double>() {
            @Override
            public Double getValue() {
                return operatingSystemMXBean.getProcessCpuLoad();
            }
        };
    }

    @Override
    protected Gauge<Double> getSystemCpuLoadGauge(final OperatingSystemMXBean operatingSystemMXBean) {
        return new Gauge<Double>() {
            @Override
            public Double getValue() {
                return operatingSystemMXBean.getSystemCpuLoad();
            }
        };
    }

    @Override
    public String toString() {
        return "CpuLoadMetricSet for Java 1.7+";
    }

}
