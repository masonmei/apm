package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_api_meta_data database table.
 */
@Entity
@Table(name = "apm_api_meta_data", indexes = {
        @Index(name = "api_meta_unique", columnList = "agent_id,api_id", unique = true)})
public class ApiMetaData extends AbstractPersistable<Long> implements ClearableAgentInfo {

    private static final long serialVersionUID = -1066319610816422700L;
    @Basic
    @Column(name = "agent_id", nullable = true, insertable = true, updatable = true)
    private Long agentId;

    @Basic
    @Column(name = "api_id", nullable = false, insertable = true, updatable = true)
    private int apiId;

    @Basic
    @Column(name = "api_info", insertable = true, updatable = true, nullable = true, length = 512)
    private String apiInfo;

    @Basic
    @Column(name = "line_number", nullable = true, insertable = true, updatable = true)
    private int lineNumber;

    @Override
    public Long getAgentId() {
        return agentId;
    }

    @Override
    public void setAgentId(Long agentId) {
        this.agentId = agentId;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("agentId", agentId)
                .add("apiId", apiId)
                .add("apiInfo", apiInfo)
                .add("lineNumber", lineNumber)
                .toString();
    }
}