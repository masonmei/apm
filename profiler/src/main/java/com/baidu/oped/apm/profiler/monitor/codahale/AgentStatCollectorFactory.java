
package com.baidu.oped.apm.profiler.monitor.codahale;

import com.baidu.oped.apm.profiler.monitor.MonitorName;
import com.baidu.oped.apm.profiler.monitor.codahale.cpu.CpuLoadCollector;
import com.baidu.oped.apm.profiler.monitor.codahale.cpu.metric.CpuLoadMetricSet;
import com.baidu.oped.apm.profiler.monitor.codahale.gc.CmsCollector;
import com.baidu.oped.apm.profiler.monitor.codahale.gc.G1Collector;
import com.baidu.oped.apm.profiler.monitor.codahale.gc.GarbageCollector;
import com.baidu.oped.apm.profiler.monitor.codahale.gc.ParallelCollector;
import com.baidu.oped.apm.profiler.monitor.codahale.gc.SerialCollector;
import com.baidu.oped.apm.profiler.monitor.codahale.gc.UnknownGarbageCollector;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.baidu.oped.apm.profiler.monitor.codahale.MetricMonitorValues.*;

/**
 * class AgentStatCollectorFactory 
 *
 * @author meidongxu@baidu.com
 */
public class AgentStatCollectorFactory {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MetricMonitorRegistry monitorRegistry;
    private final GarbageCollector garbageCollector;
    private final CpuLoadCollector cpuLoadCollector;

    public AgentStatCollectorFactory() {
        this.monitorRegistry = createRegistry();
        this.garbageCollector = createGarbageCollector();
        this.cpuLoadCollector = createCpuLoadCollector();
    }

    private MetricMonitorRegistry createRegistry() {
        final MetricMonitorRegistry monitorRegistry = new MetricMonitorRegistry();
        return monitorRegistry;
    }

    /**
     * create with garbage collector types based on metric registry keys
     */
    private GarbageCollector createGarbageCollector() {
        MetricMonitorRegistry registry = this.monitorRegistry;
        registry.registerJvmMemoryMonitor(new MonitorName(MetricMonitorValues.JVM_MEMORY));
        registry.registerJvmGcMonitor(new MonitorName(MetricMonitorValues.JVM_GC));

        Collection<String> keys = registry.getRegistry().getNames();
        GarbageCollector garbageCollectorToReturn;
        if (keys.contains(JVM_GC_SERIAL_MSC_COUNT)) {
            garbageCollectorToReturn = new SerialCollector(registry);
        } else if (keys.contains(JVM_GC_PS_MS_COUNT)) {
            garbageCollectorToReturn = new ParallelCollector(registry);
        } else if (keys.contains(JVM_GC_CMS_COUNT)) {
            garbageCollectorToReturn = new CmsCollector(registry);
        } else if (keys.contains(JVM_GC_G1_OLD_COUNT)) {
            garbageCollectorToReturn = new G1Collector(registry);
        } else {
            garbageCollectorToReturn = new UnknownGarbageCollector();
        }
        if (logger.isInfoEnabled()) {
            logger.info("found : {}", garbageCollectorToReturn);
        }
        return garbageCollectorToReturn;
    }

    private CpuLoadCollector createCpuLoadCollector() {
        CpuLoadMetricSet cpuLoadMetricSet = this.monitorRegistry.registerCpuLoadMonitor(new MonitorName(MetricMonitorValues.CPU_LOAD));
        if (logger.isInfoEnabled()) {
            logger.info("loaded : {}", cpuLoadMetricSet);
        }
        return new CpuLoadCollector(cpuLoadMetricSet);
    }

    public GarbageCollector getGarbageCollector() {
        return this.garbageCollector;
    }

    public CpuLoadCollector getCpuLoadCollector() {
        return this.cpuLoadCollector;
    }

}
