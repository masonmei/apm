package com.baidu.oped.apm.common.entity;

import com.baidu.oped.apm.common.annotation.JdbcTables;
import com.baidu.oped.apm.common.annotation.Table;

/**
 * Created by mason on 8/17/15.
 */
@Table(name = JdbcTables.ANNOTATION)
public class Annotation extends BaseEntity {

    private byte version = 0;
    private long spanId;

    private int key;

    private byte valueType;
    private byte[] byteValue;

    private Long traceEventId;
    private Long traceId;


    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public long getSpanId() {
        return spanId;
    }

    public void setSpanId(long spanId) {
        this.spanId = spanId;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public byte getValueType() {
        return valueType;
    }

    public void setValueType(byte valueType) {
        this.valueType = valueType;
    }

    public byte[] getByteValue() {
        return byteValue;
    }

    public void setByteValue(byte[] byteValue) {
        this.byteValue = byteValue;
    }

    public Long getTraceEventId() {
        return traceEventId;
    }

    public void setTraceEventId(Long traceEventId) {
        this.traceEventId = traceEventId;
    }

    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }
}
