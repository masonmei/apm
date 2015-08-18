package com.baidu.oped.apm.common.entity;

import com.baidu.oped.apm.common.annotation.JdbcTables;
import com.baidu.oped.apm.common.annotation.Table;

/**
 * Created by mason on 8/17/15.
 */
@Table(name = JdbcTables.TRACE_EVENT)
public class TraceEvent extends BaseEntity {
    private Long traceId;

    private byte version = 0;

    private String agentId;
    private String applicationId;
    private long agentStartTime;

    private String traceAgentId;
    private long traceAgentStartTime;
    private long traceTransactionSequence;

    private long spanId;
    private short sequence;

    private int startElapsed;
    private int endElapsed;

    private String rpc;
    private short serviceType;

    private String destinationId;
    private String endPoint;
    private int apiId;

    private int depth = -1;
    private long nextSpanId = -1;

    private boolean hasException;
    private int exceptionId;
    private String exceptionMessage;

    // should get exceptionClass from dao
    private String exceptionClass;
    private Long collectorAcceptTime;

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public long getAgentStartTime() {
        return agentStartTime;
    }

    public void setAgentStartTime(long agentStartTime) {
        this.agentStartTime = agentStartTime;
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

    public long getSpanId() {
        return spanId;
    }

    public void setSpanId(long spanId) {
        this.spanId = spanId;
    }

    public short getSequence() {
        return sequence;
    }

    public void setSequence(short sequence) {
        this.sequence = sequence;
    }

    public int getStartElapsed() {
        return startElapsed;
    }

    public void setStartElapsed(int startElapsed) {
        this.startElapsed = startElapsed;
    }

    public int getEndElapsed() {
        return endElapsed;
    }

    public void setEndElapsed(int endElapsed) {
        this.endElapsed = endElapsed;
    }

    public String getRpc() {
        return rpc;
    }

    public void setRpc(String rpc) {
        this.rpc = rpc;
    }

    public short getServiceType() {
        return serviceType;
    }

    public void setServiceType(short serviceType) {
        this.serviceType = serviceType;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public long getNextSpanId() {
        return nextSpanId;
    }

    public void setNextSpanId(long nextSpanId) {
        this.nextSpanId = nextSpanId;
    }

    public boolean isHasException() {
        return hasException;
    }

    public void setHasException(boolean hasException) {
        this.hasException = hasException;
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

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public void setCollectorAcceptTime(Long collectorAcceptTime) {
        this.collectorAcceptTime = collectorAcceptTime;
    }

    public Long getCollectorAcceptTime() {
        return collectorAcceptTime;
    }

    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }
}
