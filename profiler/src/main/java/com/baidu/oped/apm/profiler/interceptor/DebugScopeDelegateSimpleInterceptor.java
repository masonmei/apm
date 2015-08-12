
package com.baidu.oped.apm.profiler.interceptor;

import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.instrument.Scope;
import com.baidu.oped.apm.bootstrap.interceptor.ByteCodeMethodDescriptorSupport;
import com.baidu.oped.apm.bootstrap.interceptor.MethodDescriptor;
import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.TraceContextSupport;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;

/**
 * class DebugScopeDelegateSimpleInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class DebugScopeDelegateSimpleInterceptor implements SimpleAroundInterceptor, ByteCodeMethodDescriptorSupport, TraceContextSupport {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();
    private final SimpleAroundInterceptor delegate;
    private final Scope scope;


    public DebugScopeDelegateSimpleInterceptor(SimpleAroundInterceptor delegate, Scope scope) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }
        if (scope == null) {
            throw new NullPointerException("scope must not be null");
        }
        this.delegate = delegate;
        this.scope = scope;
    }

    @Override
    public void before(Object target, Object[] args) {
        final int push = scope.push();
        if (push != Scope.ZERO) {
            if (isDebug) {
                logger.debug("push {}. skip trace. level:{} {}", new Object[]{scope.getName(), push, delegate.getClass()});
            }
            return;
        }
        this.delegate.before(target, args);
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        final int pop = scope.pop();
        if (pop != Scope.ZERO) {
            if (isDebug) {
                logger.debug("pop {}. skip trace. level:{} {}", new Object[]{scope.getName(), pop, delegate.getClass()});
            }
            return;
        }
        this.delegate.after(target, args, result, throwable);
    }

    @Override
    public void setMethodDescriptor(MethodDescriptor descriptor) {
        if (this.delegate instanceof ByteCodeMethodDescriptorSupport) {
            ((ByteCodeMethodDescriptorSupport) this.delegate).setMethodDescriptor(descriptor);
        }
    }

    @Override
    public void setTraceContext(TraceContext traceContext) {
        if (this.delegate instanceof TraceContextSupport) {
            ((TraceContextSupport) this.delegate).setTraceContext(traceContext);
        }
    }
}
