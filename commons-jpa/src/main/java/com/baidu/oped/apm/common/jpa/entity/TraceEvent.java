package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_span_event database table.
 */
@Entity
@Table(name = "apm_span_event")
public class TraceEvent extends AbstractPersistable<Long> implements ClearableAgentInfo {

    private static final long serialVersionUID = -3749197360786535881L;
    @Basic
    @Column(name = "agent_id", nullable = true, insertable = true, updatable = true)
    private Long agentId;

    @Basic
    @Column(name = "trace_id", nullable = false, insertable = true, updatable = true)
    private Long traceId;

    @Basic
    @Column(name = "sequence", nullable = false, insertable = true, updatable = true)
    private short sequence;

    // api meta data unique id
    @Basic
    @Column(name = "api_id", nullable = false, insertable = true, updatable = true)
    private Long apiId;

    @Column(name = "collector_accept_time")
    private Long collectorAcceptTime;

    @Column(nullable = true)
    private int depth;

    @Column(name = "destination_id", length = 512)
    private String destinationId;

    @Column(name = "end_elapsed")
    private int endElapsed;

    @Column(name = "end_point", length = 512)
    private String endPoint;

    @Column(name = "exception_class", length = 512)
    private String exceptionClass;

    @Column(name = "exception_id")
    private int exceptionId;

    @Column(name = "exception_message", length = 512)
    private String exceptionMessage;

    @Column(name = "has_exception")
    private boolean hasException;

    @Column(name = "next_span_id", nullable = true)
    private long nextSpanId;

    @Column(length = 512)
    private String rpc;

    @Column(name = "service_type")
    private int serviceType;

    @Column(name = "start_elapsed")
    private int startElapsed;

    @Column(name = "trace_agent_id", length = 128)
    private String traceAgentId;

    @Column(name = "trace_agent_start_time")
    private long traceAgentStartTime;

    @Column(name = "trace_transaction_sequence")
    private long traceTransactionSequence;

    @Basic
    @Column(name = "version", nullable = true, insertable = true, updatable = true)
    private int version;

    @Override
    public Long getAgentId() {
        return agentId;
    }

    @Override
    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Long getCollectorAcceptTime() {
        return collectorAcceptTime;
    }

    public void setCollectorAcceptTime(Long collectorAcceptTime) {
        this.collectorAcceptTime = collectorAcceptTime;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public int getEndElapsed() {
        return endElapsed;
    }

    public void setEndElapsed(int endElapsed) {
        this.endElapsed = endElapsed;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public int getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(int exceptionId) {
        this.exceptionId = exceptionId;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public boolean isHasException() {
        return hasException;
    }

    public void setHasException(boolean hasException) {
        this.hasException = hasException;
    }

    public long getNextSpanId() {
        return nextSpanId;
    }

    public void setNextSpanId(long nextSpanId) {
        this.nextSpanId = nextSpanId;
    }

    public String getRpc() {
        return rpc;
    }

    public void setRpc(String rpc) {
        this.rpc = rpc;
    }

    public short getSequence() {
        return sequence;
    }

    public void setSequence(short sequence) {
        this.sequence = sequence;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getStartElapsed() {
        return startElapsed;
    }

    public void setStartElapsed(int startElapsed) {
        this.startElapsed = startElapsed;
    }

    public String getTraceAgentId() {
        return traceAgentId;
    }

    public void setTraceAgentId(String traceAgentId) {
        this.traceAgentId = traceAgentId;
    }

    public long getTraceAgentStartTime() {
        return traceAgentStartTime;
    }

    public void setTraceAgentStartTime(long traceAgentStartTime) {
        this.traceAgentStartTime = traceAgentStartTime;
    }

    public long getTraceTransactionSequence() {
        return traceTransactionSequence;
    }

    public void setTraceTransactionSequence(long traceTransactionSequence) {
        this.traceTransactionSequence = traceTransactionSequence;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("agentId", agentId)
                .add("traceId", traceId)
                .add("sequence", sequence)
                .add("apiId", apiId)
                .add("collectorAcceptTime", collectorAcceptTime)
                .add("depth", depth)
                .add("destinationId", destinationId)
                .add("endElapsed", endElapsed)
                .add("endPoint", endPoint)
                .add("exceptionClass", exceptionClass)
                .add("exceptionId", exceptionId)
                .add("exceptionMessage", exceptionMessage)
                .add("hasException", hasException)
                .add("nextSpanId", nextSpanId)
                .add("rpc", rpc)
                .add("serviceType", serviceType)
                .add("startElapsed", startElapsed)
                .add("traceAgentId", traceAgentId)
                .add("traceAgentStartTime", traceAgentStartTime)
                .add("traceTransactionSequence", traceTransactionSequence)
                .add("version", version)
                .toString();
    }
}