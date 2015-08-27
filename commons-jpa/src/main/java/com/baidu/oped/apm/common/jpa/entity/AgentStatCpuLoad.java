package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_agent_stat_cpu_load database table.
 */
@Entity
@Table(name = "apm_agent_stat_cpu_load")
public class AgentStatCpuLoad extends AbstractPersistable<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic
    @Column(name = "app_id", nullable = false, insertable = true, updatable = true)
    private Long appId;

    @Basic
    @Column(name = "instance_id", nullable = false, insertable = true, updatable = true)
    private Long instanceId;

    @Column(name = "jvm_cpu_load", nullable = false, insertable = true, updatable = true, precision = 4)
    private double jvmCpuLoad;

    @Column(name = "system_cpu_load", nullable = false, insertable = true, updatable = true, precision = 4)
    private double systemCpuLoad;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private long timestamp;

    public AgentStatCpuLoad() {
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}