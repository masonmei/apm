package com.baidu.oped.apm.bootstrap.interceptor;

import com.baidu.oped.apm.bootstrap.context.TraceContext;

/**
 * class TraceContextSupport
 *
 * @author meidongxu@baidu.com
 */
public interface TraceContextSupport {
    void setTraceContext(TraceContext traceContext);
}
