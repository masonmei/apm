package com.baidu.oped.apm.mvc.vo;

import com.baidu.oped.apm.common.utils.NumberUtils;

/**
 * class ApplicationVo
 *
 * @author meidongxu@baidu.com
 */
public final class ApplicationVo {

    private Long appId = 0L;
    private String appName = "";
    private Double responseTime = 0.0D;
    private Double cpm = 0.0D;
    private Double errorRate = 0.0D;

    public ApplicationVo() {
    }

    public ApplicationVo(long appId, String appName, double allRt, long pv, double error, long period) {
        this.appId = appId;
        this.appName = appName;
        this.responseTime = NumberUtils.format(allRt / Double.valueOf(pv));
        this.cpm = NumberUtils.format(Double.valueOf(pv) / Double.valueOf(period));
        this.errorRate = NumberUtils.format(Double.valueOf(error) / Double.valueOf(pv));
    }

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
