package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_span_event database table.
 */
@Entity
@Table(name = "apm_span_event")
public class TraceEvent extends AbstractPersistable<Long> implements Serializable {
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

    @Column(nullable = false)
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

    @Column(name = "next_span_id", nullable = false)
    private long nextSpanId;

    @Column(length = 512)
    private String rpc;

    private short sequence;

    @Column(name = "service_type")
    private int serviceType;

    @Column(name = "span_id")
    private long spanId;

    @Column(name = "start_elapsed")
    private int startElapsed;

    @Column(name = "trace_agent_id", length = 128)
    private String traceAgentId;

    @Column(name = "trace_agent_start_time")
    private long traceAgentStartTime;

    @Column(name = "trace_transaction_sequence")
    private long traceTransactionSequence;

    private int version;

    //bi-directional many-to-one association to Annotation
    @OneToMany(mappedBy = "traceEvent")
    private List<Annotation> annotations = new ArrayList<>();

    //bi-directional many-to-one association to Trace
    @ManyToOne
    @JoinColumn(name = "trace_id")
    private Trace trace;

    public TraceEvent() {
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

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getDestinationId() {
        return this.destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public int getEndElapsed() {
        return this.endElapsed;
    }

    public void setEndElapsed(int endElapsed) {
        this.endElapsed = endElapsed;
    }

    public String getEndPoint() {
        return this.endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
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

    public boolean getHasException() {
        return this.hasException;
    }

    public void setHasException(boolean hasException) {
        this.hasException = hasException;
    }

    public long getNextSpanId() {
        return this.nextSpanId;
    }

    public void setNextSpanId(long nextSpanId) {
        this.nextSpanId = nextSpanId;
    }

    public String getRpc() {
        return this.rpc;
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

    public long getSpanId() {
        return this.spanId;
    }

    public void setSpanId(long spanId) {
        this.spanId = spanId;
    }

    public int getStartElapsed() {
        return this.startElapsed;
    }

    public void setStartElapsed(int startElapsed) {
        this.startElapsed = startElapsed;
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

    public List<Annotation> getAnnotations() {
        return this.annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public Annotation addAnnotation(Annotation annotation) {
        getAnnotations().add(annotation);
        annotation.setTraceEvent(this);

        return annotation;
    }

    public Annotation removeAnnotation(Annotation annotation) {
        getAnnotations().remove(annotation);
        annotation.setTraceEvent(null);

        return annotation;
    }

    public Trace getTrace() {
        return trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }
}