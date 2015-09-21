package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_string_meta_data database table.
 */
@Entity
@Table(name = "apm_string_meta_data", indexes = {
        @Index(name = "string_meta_unique", columnList = "instance_id,start_time,string_id", unique = true)})
public class StringMetaData extends AbstractPersistable<Long> implements ClearableAgentInfo {

    @Basic
    @Column(name = "agent_id", nullable = true, insertable = true, updatable = true)
    private Long agentId;

    @Column(name = "start_time", nullable = false, insertable = true, updatable = true)
    private long startTime;

    @Column(name = "string_id", nullable = false, insertable = true, updatable = true)
    private int stringId;

    @Column(name = "string_value", insertable = true, updatable = true, length = 512)
    private String stringValue;

    @Override
    public Long getAgentId() {
        return agentId;
    }

    @Override
    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getStringId() {
        return stringId;
    }

    public void setStringId(int stringId) {
        this.stringId = stringId;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}