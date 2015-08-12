package com.baidu.oped.apm.bootstrap.interceptor;

import com.baidu.oped.apm.bootstrap.context.RecordableTrace;
import com.baidu.oped.apm.bootstrap.context.Trace;
import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;

/**
 * class SpanEventSimpleAroundInterceptor
 *
 * @author meidongxu@baidu.com
 */
public abstract class SpanEventSimpleAroundInterceptor implements SimpleAroundInterceptor,
                                                                          ByteCodeMethodDescriptorSupport,
                                                                          TraceContextSupport {
    protected final PLogger logger;
    protected final boolean isDebug;

    private MethodDescriptor descriptor;

    private TraceContext traceContext;

    protected SpanEventSimpleAroundInterceptor(Class<? extends SpanEventSimpleAroundInterceptor> childClazz) {
        this.logger = PLoggerFactory.getLogger(childClazz);
        this.isDebug = logger.isDebugEnabled();
    }

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logBeforeInterceptor(target, args);
        }

        prepareBeforeTrace(target, args);

        final Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }
        try {
            trace.traceBlockBegin();
            doInBeforeTrace(trace, target, args);
        } catch (Throwable th) {
            if (logger.isWarnEnabled()) {
                logger.warn("before. Caused:{}", th.getMessage(), th);
            }
        }
    }

    protected void logBeforeInterceptor(Object target, Object[] args) {
        logger.beforeInterceptor(target, args);
    }

    protected void prepareBeforeTrace(Object target, Object[] args) {

    }

    protected abstract void doInBeforeTrace(final RecordableTrace trace, final Object target, final Object[] args);

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logAfterInterceptor(target, args, result, throwable);
        }

        prepareAfterTrace(target, args, result, throwable);

        final Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }
        try {
            doInAfterTrace(trace, target, args, result, throwable);
        } catch (Throwable th) {
            if (logger.isWarnEnabled()) {
                logger.warn("after error. Caused:{}", th.getMessage(), th);
            }
        } finally {
            trace.traceBlockEnd();
        }
    }

    protected void logAfterInterceptor(Object target, Object[] args, Object result, Throwable throwable) {
        logger.afterInterceptor(target, args, result, throwable);
    }

    protected void prepareAfterTrace(Object target, Object[] args, Object result, Throwable throwable) {

    }

    protected abstract void doInAfterTrace(final RecordableTrace trace, final Object target, final Object[] args,
                                           final Object result, Throwable throwable);

    public MethodDescriptor getMethodDescriptor() {
        return descriptor;
    }

    @Override
    public void setMethodDescriptor(MethodDescriptor descriptor) {
        this.descriptor = descriptor;
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
