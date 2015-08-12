
package com.baidu.oped.apm.profiler.modifier.arcus.interceptor;

import com.baidu.oped.apm.bootstrap.context.Trace;
import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.interceptor.*;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.bootstrap.util.MetaObject;
import com.baidu.oped.apm.common.ServiceType;

/**
 * class FrontCacheGetFutureGetInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class FrontCacheGetFutureGetInterceptor implements SimpleAroundInterceptor, ByteCodeMethodDescriptorSupport, TraceContextSupport, TargetClassLoader {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private final MetaObject<Object> getCacheName = new MetaObject<Object>("__getCacheName");
//    private final MetaObject<Object> getCacheKey = new MetaObject<Object>("__getCacheKey");

    private MethodDescriptor methodDescriptor;
    private TraceContext traceContext;

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }

        final Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        trace.traceBlockBegin();
        trace.markBeforeTime();
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }

        final Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        try {
            trace.recordApi(methodDescriptor);

//            String cacheKey = (String) getCacheKey.invoke(target);
//            if (cacheKey != null) {
//                // annotate it.
//            }

            String cacheName = (String) getCacheName.invoke(target);
            if (cacheName != null) {
                trace.recordDestinationId(cacheName);
            }

            trace.recordServiceType(ServiceType.ARCUS_EHCACHE_FUTURE_GET);
            trace.markAfterTime();
        } finally {
            trace.traceBlockEnd();
        }
    }

    @Override
    public void setMethodDescriptor(MethodDescriptor descriptor) {
        this.methodDescriptor = descriptor;
        this.traceContext.cacheApi(descriptor);
    }

    @Override
    public void setTraceContext(TraceContext traceContext) {
        this.traceContext = traceContext;
    }

}
