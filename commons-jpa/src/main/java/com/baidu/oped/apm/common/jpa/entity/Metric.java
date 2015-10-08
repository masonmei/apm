package com.baidu.oped.apm.common.jpa.entity;

/**
 * Created by mason on 9/28/15.
 */
public interface Metric {

    Long getCount();

    void setCount(Long count);

    Double getMax();

    void setMax(Double max);

    Double getMin();

    void setMin(Double min);

    Double getSum();

    void setSum(Double sum);

    Double getAvg();
}
