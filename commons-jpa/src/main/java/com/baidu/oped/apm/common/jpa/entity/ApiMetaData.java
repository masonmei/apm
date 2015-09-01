package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_api_meta_data database table.
 */
@Entity
@Table(name = "apm_api_meta_data", indexes = {
        @Index(name = "api_meta_unique", columnList = "instance_id,start_time,api_id", unique = true)
})
public class ApiMetaData extends AbstractPersistable<Long> implements Serializable {

    @Basic
    @Column(name = "instance_id", nullable = false, insertable = true, updatable = true)
    private Long instanceId;

    @Basic
    @Column(name = "start_time", nullable = false, insertable = true, updatable = true)
    private long startTime;

    @Basic
    @Column(name = "api_id", nullable = false, insertable = true, updatable = true)
    private int apiId;

    @Basic
    @Column(name = "api_info", insertable = true, updatable = true, nullable = true, length = 512)
    private String apiInfo;

    @Basic
    @Column(name = "line_number", nullable = true, insertable = true, updatable = true)
    private int lineNumber;

    public ApiMetaData() {
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public String getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(String apiInfo) {
        this.apiInfo = apiInfo;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}