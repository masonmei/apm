package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.baidu.oped.apm.common.utils.NumberUtils;

/**
 * Created by mason on 9/28/15.
 */
@Entity
@Table(name = "apm_metric_data")
public class MetricData extends AbstractPersistable<Long> implements Metric {
    private static final long serialVersionUID = -3596909110690588963L;
    @Basic
    @Column(name = "cnt", nullable = false, insertable = true, updatable = true)
    private Long count;
    @Basic
    @Column(name = "`sum`", nullable = false, insertable = true, updatable = true)
    private Double sum;
    @Basic
    @Column(name = "`max`", nullable = false, insertable = true, updatable = true)
    private Double max;
    @Basic
    @Column(name = "`min`", nullable = false, insertable = true, updatable = true)
    private Double min;

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public Double getSum() {
        return sum;
    }

    @Override
    public void setSum(Double sum) {
        this.sum = sum;
    }

    @Override
    public Double getAvg() {
        return NumberUtils.calculateRate(sum, count);
    }

    @Override
    public Double getMax() {
        return max;
    }

    @Override
    public void setMax(Double max) {
        this.max = max;
    }

    @Override
    public Double getMin() {
        return min;
    }

    @Override
    public void setMin(Double min) {
        this.min = min;
    }
}
