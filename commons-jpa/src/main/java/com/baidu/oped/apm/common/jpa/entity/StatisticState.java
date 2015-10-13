package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 8/27/15.
 */
@Entity
@Table(name = "apm_statistic_state", indexes = {
        @Index(name = "statistic_state_unique", columnList = "statistic_type,period", unique = true)})
public class StatisticState extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 5030348537108812217L;
    @Basic
    @Column(name = "statistic_type", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private StatisticType statisticType;

    @Basic
    @Column(name = "period", nullable = false, insertable = true, updatable = true)
    private Long period;

    @Basic
    @Column(name = "timestamp", nullable = false, insertable = true, updatable = true)
    private Long timestamp;

    public StatisticType getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(StatisticType statisticType) {
        this.statisticType = statisticType;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("statisticType", statisticType)
                .add("period", period)
                .add("timestamp", timestamp)
                .toString();
    }
}
