package com.baidu.oped.apm.mvc.vo;

import com.baidu.oped.apm.utils.NumberUtils;

/**
 * Created by mason on 8/12/15.
 */
public class InstanceVo {
    private Long instanceId;
    private String instanceName;
    private Double apdex;
    private Double responseTime;
    private Double cpm;
    private Double errorRate;
    private Double cpuUsage;
    private Double memoryUsage;

    public InstanceVo(long instanceId, String instanceName, Long pv, Long satisfied, Double rt,
                      Long error, long period, double cpuUsage, double memoryUsage) {
        this.instanceId = instanceId;
        this.instanceName = instanceName;
        this.apdex = NumberUtils.format(Double.valueOf(satisfied) / Double.valueOf(pv));
        this.responseTime = NumberUtils.format(rt / pv);
        this.cpm = NumberUtils.format(Double.valueOf(pv) / Double.valueOf(period));
        this.errorRate = NumberUtils.format(Double.valueOf(error) / Double.valueOf(pv));
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Double getApdex() {
        return apdex;
    }

    public void setApdex(Double apdex) {
        this.apdex = apdex;
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
