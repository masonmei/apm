package com.baidu.oped.apm.bootstrap.context;

import java.util.TimerTask;

import com.baidu.oped.apm.bootstrap.interceptor.MethodDescriptor;
import com.baidu.oped.apm.common.AnnotationKey;
import com.baidu.oped.apm.common.ServiceType;

/**
 * class AsyncTrace
 *
 * @author meidongxu@baidu.com
 */
public interface AsyncTrace {
    public static final int STATE_INIT = 0;
    public static final int STATE_FIRE = 1;
    public static final int STATE_TIMEOUT = 2;

    int getState();

    boolean fire();

    void setTimeoutTask(TimerTask timeoutTask);

    int getAsyncId();

    void setAsyncId(int asyncId);

    Object getAttachObject();

    void setAttachObject(Object attachObject);

    void traceBlockBegin();

    void markBeforeTime();

    long getBeforeTime();

    void traceBlockEnd();

    void markAfterTime();

    void recordApi(MethodDescriptor methodDescriptor);

    void recordException(Object result);

    void recordAttribute(AnnotationKey key, String value);

    void recordAttribute(AnnotationKey key, int value);

    void recordAttribute(AnnotationKey key, Object value);

    void recordServiceType(ServiceType serviceType);

    void recordRpcName(String rpcName);

    void recordDestinationId(String destinationId);

    // TODO: final String... an aggregated input needed so we don't have to sum up endPoints
    void recordEndPoint(String endPoint);
}
