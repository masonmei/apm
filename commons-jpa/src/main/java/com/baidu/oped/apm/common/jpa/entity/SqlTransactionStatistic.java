package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 8/27/15.
 */
@Entity
@javax.persistence.Table(name = "apm_sql_transaction_statistic", schema = "", catalog = "apm")
public class SqlTransactionStatistic extends AbstractPersistable<Long> {
    @Basic
    @Column(name = "sql_transaction_id", nullable = false, insertable = true, updatable = true)
    private Long sqlTransactionId;

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
    @Column(name = "max_response_time", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double maxResponseTime;

    @Basic
    @Column(name = "min_response_time", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double minResponseTime;

    public Long getSqlTransactionId() {
        return sqlTransactionId;
    }

    public void setSqlTransactionId(Long sqlTransactionId) {
        this.sqlTransactionId = sqlTransactionId;
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
}
