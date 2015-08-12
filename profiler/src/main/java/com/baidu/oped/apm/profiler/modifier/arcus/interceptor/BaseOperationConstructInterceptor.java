
package com.baidu.oped.apm.profiler.modifier.arcus.interceptor;

import com.baidu.oped.apm.bootstrap.context.Trace;
import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.TraceContextSupport;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;

/**
 * class BaseOperationConstructInterceptor 
 *
 * @author meidongxu@baidu.com
 */
@Deprecated
public class BaseOperationConstructInterceptor implements SimpleAroundInterceptor, TraceContextSupport {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

//    private final MetaObject<Object> setAsyncTrace = new MetaObject<Object>("__setAsyncTrace", Object.class);

    private TraceContext traceContext;

    @Override
    public void before(Object target, Object[] args) {
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }

        Trace trace = traceContext.currentTraceObject();

        if (trace == null) {
            return;
        }

        // Assuming no events are missed, do not process timeout.
//        AsyncTrace asyncTrace = trace.createAsyncTrace();
//        asyncTrace.markBeforeTime();
//
//        asyncTrace.setAttachObject(new TimeObject());
//
//        setAsyncTrace.invoke(target, asyncTrace);
    }

    @Override
    public void setTraceContext(TraceContext traceContext) {

        this.traceContext = traceContext;
    }
}
