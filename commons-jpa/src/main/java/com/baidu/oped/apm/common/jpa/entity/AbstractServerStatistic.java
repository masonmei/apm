package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Base Server Statistic.
 *
 * @author mason(meidongxu@baidu.com)
 */
@MappedSuperclass
public abstract class AbstractServerStatistic extends AbstractPersistable<Long> implements ServerStatistic {
    @Basic
    @Column(name = "period", nullable = false, insertable = true, updatable = true)
    private Long period;

    @Basic
    @Column(name = "timestamp", nullable = false, insertable = true, updatable = true)
    private Long timestamp;

    @Basic
    @Column(name = "heap_used_metric_id", nullable = false, insertable = true, updatable = true)
    private Long heapUsedMetric;
    @Basic
    @Column(name = "heap_max_metric_id", nullable = false, insertable = true, updatable = true)
    private Long heapMaxMetric;
    @Basic
    @Column(name = "non_heap_used_metric_id", nullable = false, insertable = true, updatable = true)
    private Long nonHeapUsedMetric;
    @Basic
    @Column(name = "non_heap_max_metric_id", nullable = false, insertable = true, updatable = true)
    private Long nonHeapMaxMetric;
    @Basic
    @Column(name = "gc_old_count_metric_id", nullable = false, insertable = true, updatable = true)
    private Long gcOldCountMetric;
    @Basic
    @Column(name = "gc_old_time_metric_id", nullable = false, insertable = true, updatable = true)
    private Long gcOldTimeMetric;
    @Basic
    @Column(name = "jvm_cpu_metric_id", nullable = false, insertable = true, updatable = true)
    private Long jvmCpuMetric;
    @Basic
    @Column(name = "system_cpu_metric_id", nullable = false, insertable = true, updatable = true)
    private Long systemCpuMetric;

    @Override
    public Long getPeriod() {
        return period;
    }

    @Override
    public void setPeriod(Long period) {
        this.period = period;
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getHeapUsedMetric() {
        return heapUsedMetric;
    }

    public void setHeapUsedMetric(Long heapUsedMetric) {
        this.heapUsedMetric = heapUsedMetric;
    }

    public Long getHeapMaxMetric() {
        return heapMaxMetric;
    }

    public void setHeapMaxMetric(Long heapMaxMetric) {
        this.heapMaxMetric = heapMaxMetric;
    }

    public Long getNonHeapUsedMetric() {
        return nonHeapUsedMetric;
    }

    public void setNonHeapUsedMetric(Long nonHeapUsedMetric) {
        this.nonHeapUsedMetric = nonHeapUsedMetric;
    }

    public Long getNonHeapMaxMetric() {
        return nonHeapMaxMetric;
    }

    public void setNonHeapMaxMetric(Long nonHeapMaxMetric) {
        this.nonHeapMaxMetric = nonHeapMaxMetric;
    }

    public Long getGcOldCountMetric() {
        return gcOldCountMetric;
    }

    public void setGcOldCountMetric(Long gcOldCountMetric) {
        this.gcOldCountMetric = gcOldCountMetric;
    }

    public Long getGcOldTimeMetric() {
        return gcOldTimeMetric;
    }

    public void setGcOldTimeMetric(Long gcOldTimeMetric) {
        this.gcOldTimeMetric = gcOldTimeMetric;
    }

    public Long getJvmCpuMetric() {
        return jvmCpuMetric;
    }

    public void setJvmCpuMetric(Long jvmCpuMetric) {
        this.jvmCpuMetric = jvmCpuMetric;
    }

    public Long getSystemCpuMetric() {
        return systemCpuMetric;
    }

    public void setSystemCpuMetric(Long systemCpuMetric) {
        this.systemCpuMetric = systemCpuMetric;
    }
}
