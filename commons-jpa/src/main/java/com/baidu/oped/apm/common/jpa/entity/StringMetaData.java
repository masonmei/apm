package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_string_meta_data database table.
 */
@Entity
@Table(name = "apm_string_meta_data")
public class StringMetaData extends AbstractPersistable<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "agent_id", nullable = false, length = 128)
    private String agentId;

    @Column(name = "start_time", nullable = false)
    private long startTime;

    @Column(name = "string_id", nullable = false)
    private int stringId;

    @Column(name = "string_value", length = 512)
    private String stringValue;

    public StringMetaData() {
    }

    public String getAgentId() {
        return this.agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getStringId() {
        return this.stringId;
    }

    public void setStringId(int stringId) {
        this.stringId = stringId;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

}