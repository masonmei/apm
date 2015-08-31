package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 8/27/15.
 */
@Entity
@Table(name = "apm_external_transaction_statistic", indexes = {
    @Index(name = "ext_tran_statistic_point_unique", columnList = "external_transaction_id,period,timestamp",
           unique = true)
})
public class ExternalTransactionStatistic extends AbstractPersistable<Long> implements BaseStatistic {
    @Basic
    @Column(name = "external_transaction_id", nullable = false, insertable = true, updatable = true)
    private Long externalTransactionId;

    @Basic
    @Column(name = "period", nullable = false, insertable = true, updatable = true)
    private Integer period;

    @Basic
    @Column(name = "timestamp", nullable = false, insertable = true, updatable = true)
    private Long timestamp;

    @Basic
    @Column(name = "sum_response_time", nullable = true, insertable = true, updatable = true, precision = 4)
    private Double sumResponseTime;

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

    public Long getExternalTransactionId() {
        return externalTransactionId;
    }

    public void setExternalTransactionId(Long externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
    }

    @Override
    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Double getSumResponseTime() {
        return sumResponseTime;
    }

    public void setSumResponseTime(Double sumResponseTime) {
        this.sumResponseTime = sumResponseTime;
    }

    @Override
    public Long getPv() {
        return pv;
    }

    public void setPv(Long pv) {
        this.pv = pv;
    }

    @Override
    public Long getError() {
        return error;
    }

    public void setError(Long error) {
        this.error = error;
    }

    @Override
    public Long getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(Long satisfied) {
        this.satisfied = satisfied;
    }

    @Override
    public Long getTolerated() {
        return tolerated;
    }

    public void setTolerated(Long tolerated) {
        this.tolerated = tolerated;
    }

    @Override
    public Long getFrustrated() {
        return frustrated;
    }

    public void setFrustrated(Long frustrated) {
        this.frustrated = frustrated;
    }

    @Override
    public Double getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Double maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    @Override
    public Double getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(Double minResponseTime) {
        this.minResponseTime = minResponseTime;
    }
}
