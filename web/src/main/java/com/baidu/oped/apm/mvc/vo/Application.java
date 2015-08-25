
package com.baidu.oped.apm.mvc.vo;

import com.baidu.oped.apm.common.ServiceType;

/**
 * class Application 
 *
 * @author meidongxu@baidu.com
 */
public final class Application {
    private Long appId;
    private String appName;
    private Double responseTime;
    private Double cpm;
    private Double errorRate;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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
}
