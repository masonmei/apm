package com.baidu.oped.apm.bootstrap.interceptor;

import com.baidu.oped.apm.bootstrap.context.RecordableTrace;
import com.baidu.oped.apm.bootstrap.context.Trace;
import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;

/**
 * class SpanSimpleAroundInterceptor
 *
 * @author meidongxu@baidu.com
 */
public abstract class SpanSimpleAroundInterceptor implements SimpleAroundInterceptor, ByteCodeMethodDescriptorSupport,
                                                                     TraceContextSupport {
    protected final PLogger logger;
    protected final boolean isDebug;

    private MethodDescriptor methodDescriptor;

    private TraceContext traceContext;

    protected SpanSimpleAroundInterceptor(Class<? extends SpanSimpleAroundInterceptor> childClazz) {
        this.logger = PLoggerFactory.getLogger(childClazz);
        this.isDebug = logger.isDebugEnabled();
    }

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }

        try {
            final Trace trace = createTrace(target, args);
            if (trace == null) {
                return;
            }
            // TODO STATDISABLE this logic was added to disable statstics tracing
            if (!trace.canSampled()) {
                return;
            }
            //------------------------------------------------------
            doInBeforeTrace(trace, target, args);
        } catch (Throwable th) {
            if (logger.isWarnEnabled()) {
                logger.warn("before. Caused:{}", th.getMessage(), th);
            }
        }
    }

    protected abstract void doInBeforeTrace(final RecordableTrace trace, Object target, final Object[] args);

    protected abstract Trace createTrace(final Object target, final Object[] args);

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }

        final Trace trace = traceContext.currentRawTraceObject();
        if (trace == null) {
            return;
        }
        traceContext.detachTraceObject();
        // TODO STATDISABLE this logic was added to disable statstics tracing
        if (!trace.canSampled()) {
            return;
        }
        //------------------------------------------------------
        try {
            doInAfterTrace(trace, target, args, result, throwable);
        } catch (Throwable th) {
            if (logger.isWarnEnabled()) {
                logger.warn("after. Caused:{}", th.getMessage(), th);
            }
        } finally {
            trace.traceRootBlockEnd();
        }
    }

    protected abstract void doInAfterTrace(final RecordableTrace trace, final Object target, final Object[] args,
                                           final Object result, Throwable throwable);

    public MethodDescriptor getMethodDescriptor() {
        return methodDescriptor;
    }

    @Override
    public void setMethodDescriptor(MethodDescriptor descriptor) {
        this.methodDescriptor = descriptor;
        this.traceContext.cacheApi(descriptor);
    }

    public TraceContext getTraceContext() {
        return traceContext;
    }

    @Override
    public void setTraceContext(TraceContext traceContext) {
        this.traceContext = traceContext;
    }
}
