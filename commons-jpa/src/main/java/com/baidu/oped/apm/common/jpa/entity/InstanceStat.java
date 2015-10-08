package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 9/2/15.
 */
@Entity
@Table(name = "apm_instance_stat", indexes = {
        @Index(name = "instance_stat_unique", columnList = "agent_id,timestamp", unique = true)})
public class InstanceStat extends AbstractPersistable<Long> implements ClearableAgentInfo {

    private static final long serialVersionUID = 2134584337846641478L;
    @Basic
    @Column(name = "agent_id", nullable = true, insertable = true, updatable = true)
    private Long agentId;

    @Column(name = "timestamp", nullable = false, updatable = true)
    private Long timestamp;

    @Column(name = "jvm_cpu_load", nullable = true, insertable = true, updatable = true, precision = 4)
    private double jvmCpuLoad;

    @Column(name = "system_cpu_load", nullable = true, insertable = true, updatable = true, precision = 4)
    private double systemCpuLoad;

    @Column(name = "gc_type", nullable = false, length = 64)
    private String gcType;

    @Column(name = "jvm_gc_old_count", nullable = true, insertable = true, updatable = true)
    private long jvmGcOldCount;

    @Column(name = "jvm_gc_old_time", nullable = true, insertable = true, updatable = true)
    private long jvmGcOldTime;

    @Column(name = "jvm_memory_heap_max", nullable = true, insertable = true, updatable = true)
    private long jvmMemoryHeapMax;

    @Column(name = "jvm_memory_heap_used", nullable = true, insertable = true, updatable = true)
    private long jvmMemoryHeapUsed;

    @Column(name = "jvm_memory_non_heap_max", nullable = true, insertable = true, updatable = true)
    private long jvmMemoryNonHeapMax;

    @Column(name = "jvm_memory_non_heap_used", nullable = true, insertable = true, updatable = true)
    private long jvmMemoryNonHeapUsed;

    @Override
    public Long getAgentId() {
        return agentId;
    }

    @Override
    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public double getJvmCpuLoad() {
        return jvmCpuLoad;
    }

    public void setJvmCpuLoad(double jvmCpuLoad) {
        this.jvmCpuLoad = jvmCpuLoad;
    }

    public double getSystemCpuLoad() {
        return systemCpuLoad;
    }

    public void setSystemCpuLoad(double systemCpuLoad) {
        this.systemCpuLoad = systemCpuLoad;
    }

    public String getGcType() {
        return gcType;
    }

    public void setGcType(String gcType) {
        this.gcType = gcType;
    }

    public long getJvmGcOldCount() {
        return jvmGcOldCount;
    }

    public void setJvmGcOldCount(long jvmGcOldCount) {
        this.jvmGcOldCount = jvmGcOldCount;
    }

    public long getJvmGcOldTime() {
        return jvmGcOldTime;
    }

    public void setJvmGcOldTime(long jvmGcOldTime) {
        this.jvmGcOldTime = jvmGcOldTime;
    }

    public long getJvmMemoryHeapMax() {
        return jvmMemoryHeapMax;
    }

    public void setJvmMemoryHeapMax(long jvmMemoryHeapMax) {
        this.jvmMemoryHeapMax = jvmMemoryHeapMax;
    }

    public long getJvmMemoryHeapUsed() {
        return jvmMemoryHeapUsed;
    }

    public void setJvmMemoryHeapUsed(long jvmMemoryHeapUsed) {
        this.jvmMemoryHeapUsed = jvmMemoryHeapUsed;
    }

    public long getJvmMemoryNonHeapMax() {
        return jvmMemoryNonHeapMax;
    }

    public void setJvmMemoryNonHeapMax(long jvmMemoryNonHeapMax) {
        this.jvmMemoryNonHeapMax = jvmMemoryNonHeapMax;
    }

    public long getJvmMemoryNonHeapUsed() {
        return jvmMemoryNonHeapUsed;
    }

    public void setJvmMemoryNonHeapUsed(long jvmMemoryNonHeapUsed) {
        this.jvmMemoryNonHeapUsed = jvmMemoryNonHeapUsed;
    }
}
