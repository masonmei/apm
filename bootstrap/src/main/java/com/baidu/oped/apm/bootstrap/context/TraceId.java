package com.baidu.oped.apm.bootstrap.context;

/**
 * class TraceId
 *
 * @author meidongxu@baidu.com
 */
public interface TraceId {

    TraceId getNextTraceId();

    long getSpanId();

    String getTransactionId();

    String getAgentId();

    long getAgentStartTime();

    long getTransactionSequence();

    long getParentSpanId();

    short getFlags();

    boolean isRoot();
}
