package com.baidu.oped.apm.common.jpa.entity;

/**
 * Created by mason on 8/31/15.
 */
public interface HostStatistic extends Statistic {

    Double getCpuUsage();

    void setCpuUsage(Double cpuUsage);

    Double getMemoryUsage();

    void setMemoryUsage(Double memoryUsage);
}
