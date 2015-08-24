package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_annotation database table.
 */
@Entity
@Table(name = "apm_annotation")
public class Annotation extends AbstractPersistable<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Lob
    @Column(name = "byte_value")
    private byte[] byteValue;

    @Column(name = "`key`")
    private int key;

    @Column(name = "value_type")
    private byte valueType;

    //bi-directional many-to-one association to Trace
    @ManyToOne
    @JoinColumn(name = "trace_id")
    private Trace trace;

    //bi-directional many-to-one association to TraceEvent
    @ManyToOne
    @JoinColumn(name = "trace_event_id")
    private TraceEvent traceEvent;

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

    public Trace getTrace() {
        return trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }

    public TraceEvent getTraceEvent() {
        return this.traceEvent;
    }

    public void setTraceEvent(TraceEvent traceEvent) {
        this.traceEvent = traceEvent;
    }

}