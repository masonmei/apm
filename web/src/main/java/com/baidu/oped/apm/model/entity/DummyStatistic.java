package com.baidu.oped.apm.model.entity;

import com.baidu.oped.apm.common.jpa.entity.CommonStatistic;
import com.baidu.oped.apm.common.jpa.entity.HostStatistic;

/**
 * Created by mason on 9/7/15.
 */
public class DummyStatistic implements CommonStatistic, HostStatistic {
    private Long period;
    private Long timestamp;

    @Override
    public Double getSumResponseTime() {
        return null;
    }

    @Override
    public void setSumResponseTime(Double sumResponseTime) {

    }

    @Override
    public Double getMaxResponseTime() {
        return null;
    }

    @Override
    public void setMaxResponseTime(Double maxResponseTime) {

    }

    @Override
    public Double getMinResponseTime() {
        return null;
    }

    @Override
    public void setMinResponseTime(Double minResponseTime) {

    }

    @Override
    public Long getPv() {
        return null;
    }

    @Override
    public void setPv(Long pv) {

    }

    @Override
    public Long getError() {
        return null;
    }

    @Override
    public void setError(Long error) {

    }

    @Override
    public Long getSatisfied() {
        return null;
    }

    @Override
    public void setSatisfied(Long satisfied) {

    }

    @Override
    public Long getTolerated() {
        return null;
    }

    @Override
    public void setTolerated(Long tolerated) {

    }

    @Override
    public Long getFrustrated() {
        return null;
    }

    @Override
    public void setFrustrated(Long frustrated) {

    }

    @Override
    public Double getCpuUsage() {
        return null;
    }

    @Override
    public void setCpuUsage(Double cpuUsage) {

    }

    @Override
    public Double getMemoryUsage() {
        return null;
    }

    @Override
    public void setMemoryUsage(Double memoryUsage) {

    }

    @Override
    public Long getPeriod() {
        return period;
    }

    @Override
    public void setPeriod(Long period) {
        this.period = period;
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
