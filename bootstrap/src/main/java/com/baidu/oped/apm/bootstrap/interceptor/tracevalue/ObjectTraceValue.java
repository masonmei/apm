package com.baidu.oped.apm.bootstrap.interceptor.tracevalue;

/**
 * class ObjectTraceValue
 *
 * @author meidongxu@baidu.com
 */
public interface ObjectTraceValue extends TraceValue {

    void __setTraceObject(Object value);

    Object __getTraceObject();
}
