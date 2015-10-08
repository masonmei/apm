package com.baidu.oped.apm.common.jpa.entity;

/**
 * Created by mason on 9/28/15.
 */
public interface ServerStatistic extends Statistic {

    Long getHeapUsedMetric();

    Long getHeapMaxMetric();

    Long getNonHeapUsedMetric();

    Long getNonHeapMaxMetric();

    Long getGcOldCountMetric();

    Long getGcOldTimeMetric();

    Long getJvmCpuMetric();

    Long getSystemCpuMetric();
}
