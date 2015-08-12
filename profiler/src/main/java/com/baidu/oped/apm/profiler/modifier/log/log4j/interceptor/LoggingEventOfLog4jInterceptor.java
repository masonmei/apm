package com.baidu.oped.apm.profiler.modifier.log.log4j.interceptor;

import org.apache.log4j.MDC;

import com.baidu.oped.apm.bootstrap.context.Trace;
import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.TargetClassLoader;
import com.baidu.oped.apm.bootstrap.interceptor.TraceContextSupport;
import com.baidu.oped.apm.profiler.modifier.log.MdcKey;

/**
 * class LoggingEventOfLog4jInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class LoggingEventOfLog4jInterceptor implements SimpleAroundInterceptor, TraceContextSupport, TargetClassLoader {

    private TraceContext traceContext;

    @Override
    public void before(Object target, Object[] args) {
        Trace trace = traceContext.currentTraceObject();
        
        if (trace == null) {
            MDC.remove(MdcKey.TRANSACTION_ID);
            MDC.remove(MdcKey.SPAN_ID);
            return;
        } else {
            MDC.put(MdcKey.TRANSACTION_ID, trace.getTraceId().getTransactionId());
            MDC.put(MdcKey.SPAN_ID, String.valueOf(trace.getTraceId().getSpanId()));
        }
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
    }

    @Override
    public void setTraceContext(TraceContext traceContext) {
        this.traceContext = traceContext;
    }

}
