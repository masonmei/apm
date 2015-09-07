package com.baidu.oped.apm.common.jpa.entity;

/**
 * Created by mason on 8/31/15.
 */
public interface Statistic {

    Long getPeriod();

    void setPeriod(Long period);

    Long getTimestamp();

    void setTimestamp(Long timestamp);

}
