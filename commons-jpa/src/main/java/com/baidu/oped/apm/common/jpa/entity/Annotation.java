package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_annotation database table.
 */
@Entity
@Table(name = "apm_annotation")
public class Annotation extends AbstractPersistable<Long> implements Serializable {

    @Lob
    @Column(name = "byte_value", nullable = true, insertable = true, updatable = true)
    private byte[] byteValue;

    @Column(name = "`key`", nullable = true, insertable = true, updatable = true)
    private int key;

    @Column(name = "value_type", nullable = true, insertable = true, updatable = true)
    private byte valueType;

    @Basic
    @Column(name = "trace_id", nullable = true, insertable = true, updatable = true)
    private Long traceId;

    @Basic
    @Column(name = "trace_event_id", nullable = true, insertable = true, updatable = true)
    private Long traceEventId;

    public Annotation() {
    }

    public byte[] getByteValue() {
        return this.byteValue;
    }

    public void setByteValue(byte[] byteValue) {
        this.byteValue = byteValue;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public byte getValueType() {
        return this.valueType;
    }

    public void setValueType(byte valueType) {
        this.valueType = valueType;
    }

    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    public Long getTraceEventId() {
        return traceEventId;
    }

    public void setTraceEventId(Long traceEventId) {
        this.traceEventId = traceEventId;
    }
}