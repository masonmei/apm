package com.baidu.oped.apm.mvc.vo;

/**
 * Created by mason on 8/25/15.
 */
public class DatabaseServiceVo {
    private Long appId;
    private Long instanceId;
    private Long sqlTransactionId;
    private String sql;
    private Long pv;
    private Double cpm;
    private Double responseTime;
    private Double maxResponseTime;
    private Double minResponseTime;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long getSqlTransactionId() {
        return sqlTransactionId;
    }

    public void setSqlTransactionId(Long sqlTransactionId) {
        this.sqlTransactionId = sqlTransactionId;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Long getPv() {
        return pv;
    }

    public void setPv(Long pv) {
        this.pv = pv;
    }

    public Double getCpm() {
        return cpm;
    }

    public void setCpm(Double cpm) {
        this.cpm = cpm;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }

    public Double getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Double maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public Double getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(Double minResponseTime) {
        this.minResponseTime = minResponseTime;
    }
}
