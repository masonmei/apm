package com.baidu.oped.apm.mvc.vo;

/**
 * Created by mason on 8/12/15.
 */
public class Instance {
    private String instanceId;
    private String name;
    private double apdex;
    private double responseTime;
    private double countPerMins;
    private double errorRate;
    private double cpuUsage;
    private double memoryUsage;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getApdex() {
        return apdex;
    }

    public void setApdex(double apdex) {
        this.apdex = apdex;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    public double getCountPerMins() {
        return countPerMins;
    }

    public void setCountPerMins(double countPerMins) {
        this.countPerMins = countPerMins;
    }

    public double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }
}
