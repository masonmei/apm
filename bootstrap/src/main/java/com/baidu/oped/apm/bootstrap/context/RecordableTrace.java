package com.baidu.oped.apm.bootstrap.context;

import com.baidu.oped.apm.bootstrap.interceptor.MethodDescriptor;
import com.baidu.oped.apm.common.AnnotationKey;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.common.util.ParsingResult;

/**
 * class RecordableTrace
 *
 * @author meidongxu@baidu.com
 */
public interface RecordableTrace {

    void markBeforeTime();

    long getBeforeTime();

    void markAfterTime();

    long getAfterTime();

    TraceId getTraceId();

    boolean canSampled();

    boolean isRoot();

    ServiceType getServiceType();

    void recordException(Throwable throwable);

    void recordApi(MethodDescriptor methodDescriptor);

    void recordApi(MethodDescriptor methodDescriptor, Object[] args);

    void recordApi(MethodDescriptor methodDescriptor, Object args, int index);

    void recordApi(MethodDescriptor methodDescriptor, Object[] args, int start, int end);

    void recordApiCachedString(MethodDescriptor methodDescriptor, String args, int index);

    ParsingResult recordSqlInfo(String sql);

    void recordSqlParsingResult(ParsingResult parsingResult);

    void recordSqlParsingResult(ParsingResult parsingResult, String bindValue);

    void recordAttribute(AnnotationKey key, String value);

    void recordAttribute(AnnotationKey key, int value);

    void recordAttribute(AnnotationKey key, Object value);

    void recordServiceType(ServiceType serviceType);

    void recordRpcName(String rpc);

    void recordDestinationId(String destinationId);

    void recordEndPoint(String endPoint);

    void recordRemoteAddress(String remoteAddress);

    void recordNextSpanId(long spanId);

    void recordParentApplication(String parentApplicationName, short parentApplicationType);

    /**
     * when WAS_A sends a request to WAS_B, WAS_A stores its own data through parameters sent by WAS_B.
     * This data is needed to extract caller/callee relationship.
     *
     * @param host (we need to extract hostname from the request URL)
     */
    void recordAcceptorHost(String host);

    int getStackFrameId();
}
