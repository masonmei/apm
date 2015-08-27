package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 8/27/15.
 */
@Entity
@javax.persistence.Table(name = "apm_instance_statistic", schema = "", catalog = "apm")
public class InstanceStatistic extends AbstractPersistable<Long> implements Serializable {

    @Basic
    @javax.persistence.Column(name = "instance_id", nullable = false, insertable = true, updatable = true)
    private Long instanceId;

    @Basic
    @Column(name = "period", nullable = false, insertable = true, updatable = true)
    private Integer period;

    @Basic
    @Column(name = "timestamp", nullable = false, insertable = true, updatable = true)
    private Long timestamp;

    @Basic
    @Column(name = "response_time", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double responseTime;

    @Basic
    @Column(name = "cpm", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double cpm;

    @Basic
    @Column(name = "error_rate", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double errorRate;

    @Basic
    @Column(name = "apdex", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double apdex;

    @Basic
    @Column(name = "satisfied", nullable = true, insertable = true, updatable = true)
    private Long satisfied;

    @Basic
    @Column(name = "tolerated", nullable = true, insertable = true, updatable = true)
    private Long tolerated;

    @Basic
    @Column(name = "frustrated", nullable = true, insertable = true, updatable = true)
    private Long frustrated;

    @Basic
    @Column(name = "max_response_time", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double maxResponseTime;

    @Basic
    @Column(name = "min_response_time", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double minResponseTime;

    @Basic
    @Column(name = "cpu_usage", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double cpuUsage;

    @Basic
    @Column(name = "memory_usage", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double memoryUsage;

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }

    public Double getCpm() {
        return cpm;
    }

    public void setCpm(Double cpm) {
        this.cpm = cpm;
    }

    public Double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(Double errorRate) {
        this.errorRate = errorRate;
    }

    public Double getApdex() {
        return apdex;
    }

    public void setApdex(Double apdex) {
        this.apdex = apdex;
    }

    public Long getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(Long satisfied) {
        this.satisfied = satisfied;
    }

    public Long getTolerated() {
        return tolerated;
    }

    public void setTolerated(Long tolerated) {
        this.tolerated = tolerated;
    }

    public Long getFrustrated() {
        return frustrated;
    }

    public void setFrustrated(Long frustrated) {
        this.frustrated = frustrated;
    }

    public Double getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Double maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public Double getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(Double minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }
}
