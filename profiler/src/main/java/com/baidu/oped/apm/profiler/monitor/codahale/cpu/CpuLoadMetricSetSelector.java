
package com.baidu.oped.apm.profiler.monitor.codahale.cpu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.common.util.JvmUtils;
import com.baidu.oped.apm.common.util.JvmVersion;
import com.baidu.oped.apm.profiler.monitor.codahale.cpu.metric.CpuLoadMetricSet;
import com.baidu.oped.apm.profiler.monitor.codahale.cpu.metric.DefaultCpuLoadMetricSet;
import com.baidu.oped.apm.profiler.monitor.codahale.cpu.metric.EmptyCpuLoadMetricSet;

/**
 * class CpuLoadMetricSetSelector 
 *
 * @author meidongxu@baidu.com
 */
public class CpuLoadMetricSetSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(CpuLoadMetricSetSelector.class);

    private static final String OPTIONAL_CPU_LOAD_METRIC_SET_CLASSPATH = "com.baidu.oped.apm.profiler.monitor.codahale.cpu.metric.EnhancedCpuLoadMetricSet";

    private CpuLoadMetricSetSelector() {
        throw new IllegalAccessError();
    }

    public static CpuLoadMetricSet getCpuLoadMetricSet() {
        if (canLoadOptionalPackage()) {
            CpuLoadMetricSet optionalPackage = loadOptionalPackage();
            if (optionalPackage != null) {
                return optionalPackage;
            }
        }
        if (canLoadDefault()) {
            return new DefaultCpuLoadMetricSet();
        } else {
            return new EmptyCpuLoadMetricSet();
        }
    }

    private static CpuLoadMetricSet loadOptionalPackage() {
        try {
            @SuppressWarnings("unchecked")
            Class<CpuLoadMetricSet> clazz = (Class<CpuLoadMetricSet>)Class.forName(OPTIONAL_CPU_LOAD_METRIC_SET_CLASSPATH);
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                LOGGER.error("Error instantiating optional package.", e);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.info("Optional package not found.");
        }
        return null;
    }

    private static boolean canLoadOptionalPackage() {
        // Check if JDK version is >= 1.7
        return JvmUtils.supportsVersion(JvmVersion.JAVA_7);
    }

    private static boolean canLoadDefault() {
        return JvmUtils.getVersion() != JvmVersion.UNSUPPORTED;
    }
}
