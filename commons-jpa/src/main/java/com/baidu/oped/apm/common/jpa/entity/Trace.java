package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_span database table.
 */
@Entity
@Table(name = "apm_span")
public class Trace extends AbstractPersistable<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "agent_id", nullable = false, length = 128)
    private String agentId;

    @Column(name = "agent_start_time", nullable = false)
    private long agentStartTime;

    @Column(name = "api_id")
    private int apiId;

    @Column(name = "application_id", nullable = false, length = 128)
    private String applicationId;

    @Column(name = "collector_accept_time")
    private long collectorAcceptTime;

    private int elapsed;

    @Column(name = "end_point", length = 512)
    private String endPoint;

    @Column(name = "err_code")
    private int errCode;

    @Column(name = "exception_class", length = 512)
    private String exceptionClass;

    @Column(name = "exception_id")
    private int exceptionId;

    @Column(name = "exception_message", length = 512)
    private String exceptionMessage;

    private short flag;

    @Column(name = "has_exception", nullable = false)
    private boolean hasException = false;

    @Column(name = "parent_span_id")
    private long parentSpanId;

    @Column(name = "remote_addr", length = 512)
    private String remoteAddr;

    @Column(length = 512)
    private String rpc;

    @Column(name = "service_type")
    private int serviceType;

    @Column(name = "span_id")
    private long spanId;

    @Column(name = "start_time")
    private long startTime;

    @Column(name = "trace_agent_id", length = 128)
    private String traceAgentId;

    @Column(name = "trace_agent_start_time")
    private long traceAgentStartTime;

    @Column(name = "trace_transaction_sequence")
    private long traceTransactionSequence;

    @Basic
    @Column(name = "version", nullable = false, insertable = true, updatable = true)
    private int version;



    public Trace() {
    }

    public String getAgentId() {
        return this.agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public long getAgentStartTime() {
        return this.agentStartTime;
    }

    public void setAgentStartTime(long agentStartTime) {
        this.agentStartTime = agentStartTime;
    }

    public int getApiId() {
        return this.apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public String getApplicationId() {
        return this.applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public long getCollectorAcceptTime() {
        return this.collectorAcceptTime;
    }

    public void setCollectorAcceptTime(long collectorAcceptTime) {
        this.collectorAcceptTime = collectorAcceptTime;
    }

    public int getElapsed() {
        return this.elapsed;
    }

    public void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }

    public String getEndPoint() {
        return this.endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public int getErrCode() {
        return this.errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getExceptionClass() {
        return this.exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public int getExceptionId() {
        return this.exceptionId;
    }

    public void setExceptionId(int exceptionId) {
        this.exceptionId = exceptionId;
    }

    public String getExceptionMessage() {
        return this.exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public short getFlag() {
        return flag;
    }

    public void setFlag(short flag) {
        this.flag = flag;
    }

    public boolean getHasException() {
        return this.hasException;
    }

    public void setHasException(boolean hasException) {
        this.hasException = hasException;
    }

    public long getParentSpanId() {
        return this.parentSpanId;
    }

    public void setParentSpanId(long parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRpc() {
        return this.rpc;
    }

    public void setRpc(String rpc) {
        this.rpc = rpc;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public long getSpanId() {
        return this.spanId;
    }

    public void setSpanId(long spanId) {
        this.spanId = spanId;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getTraceAgentId() {
        return this.traceAgentId;
    }

    public void setTraceAgentId(String traceAgentId) {
        this.traceAgentId = traceAgentId;
    }

    public long getTraceAgentStartTime() {
        return this.traceAgentStartTime;
    }

    public void setTraceAgentStartTime(long traceAgentStartTime) {
        this.traceAgentStartTime = traceAgentStartTime;
    }

    public long getTraceTransactionSequence() {
        return this.traceTransactionSequence;
    }

    public void setTraceTransactionSequence(long traceTransactionSequence) {
        this.traceTransactionSequence = traceTransactionSequence;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isHasException() {
        return hasException;
    }
}