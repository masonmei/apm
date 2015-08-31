package com.baidu.oped.apm.common.jpa.entity;

/**
 * Created by mason on 8/31/15.
 */
public interface HostStatistic {

    Integer getPeriod();

    void setPeriod(Integer period);

    Long getTimestamp();

    void setTimestamp(Long timestamp);

    Double getCpuUsage();

    void setCpuUsage(Double cpuUsage);

    Double getMemoryUsage();

    void setMemoryUsage(Double memoryUsage);
}
