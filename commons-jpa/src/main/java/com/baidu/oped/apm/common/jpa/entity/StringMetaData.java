package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_string_meta_data database table.
 */
@Entity
@Table(name = "apm_string_meta_data")
public class StringMetaData extends AbstractPersistable<Long> {

    @Column(name = "instance_id", nullable = false, insertable = true, updatable = true)
    private Long instanceId;

    @Column(name = "start_time", nullable = false, insertable = true, updatable = true)
    private long startTime;

    @Column(name = "string_id", nullable = false, insertable = true, updatable = true)
    private int stringId;

    @Column(name = "string_value", insertable = true, updatable = true, length = 512)
    private String stringValue;

    public StringMetaData() {
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
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