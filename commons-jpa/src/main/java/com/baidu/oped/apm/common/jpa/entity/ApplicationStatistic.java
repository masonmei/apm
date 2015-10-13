package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 8/27/15.
 */
@Entity
@Table(name = "apm_application_statistic", indexes = {
        @Index(name = "app_statistic_point_unique", columnList = "app_id,period,timestamp", unique = true)})
public class ApplicationStatistic extends AbstractPersistable<Long> implements CommonStatistic, HostStatistic {

    private static final long serialVersionUID = -1725813356022430903L;
    @Basic
    @Column(name = "app_id", nullable = false, insertable = true, updatable = true)
    private Long appId;

    @Basic
    @Column(name = "period", nullable = false, insertable = true, updatable = true)
    private Long period;

    @Basic
    @Column(name = "timestamp", nullable = false, insertable = true, updatable = true)
    private Long timestamp;

    @Basic
    @Column(name = "sum_response_time", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double sumResponseTime;

    @Basic
    @Column(name = "max_response_time", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double maxResponseTime;

    @Basic
    @Column(name = "min_response_time", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double minResponseTime;

    @Basic
    @Column(name = "pv", nullable = true, insertable = true, updatable = true)
    private Long pv;

    @Basic
    @Column(name = "error", nullable = true, insertable = true, updatable = true)
    private Long error;

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
    @Column(name = "cpu_usage", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double cpuUsage;

    @Basic
    @Column(name = "memory_usage", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double memoryUsage;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getSumResponseTime() {
        return sumResponseTime;
    }

    public void setSumResponseTime(Double sumResponseTime) {
        this.sumResponseTime = sumResponseTime;
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

    public Long getPv() {
        return pv;
    }

    public void setPv(Long pv) {
        this.pv = pv;
    }

    public Long getError() {
        return error;
    }

    public void setError(Long error) {
        this.error = error;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("appId", appId)
                .add("period", period)
                .add("timestamp", timestamp)
                .add("sumResponseTime", sumResponseTime)
                .add("maxResponseTime", maxResponseTime)
                .add("minResponseTime", minResponseTime)
                .add("pv", pv)
                .add("error", error)
                .add("satisfied", satisfied)
                .add("tolerated", tolerated)
                .add("frustrated", frustrated)
                .add("cpuUsage", cpuUsage)
                .add("memoryUsage", memoryUsage)
                .toString();
    }
}
