
package com.baidu.oped.apm.profiler.interceptor;

import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.instrument.Scope;
import com.baidu.oped.apm.bootstrap.interceptor.StaticAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.TraceContextSupport;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;

/**
 * class DebugScopeDelegateStaticInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class DebugScopeDelegateStaticInterceptor implements StaticAroundInterceptor, TraceContextSupport {
    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();
    private final StaticAroundInterceptor delegate;
    private final Scope scope;


    public DebugScopeDelegateStaticInterceptor(StaticAroundInterceptor delegate, Scope scope) {
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
    public void before(Object target, String className, String methodName, String parameterDescription, Object[] args) {
        final int push = scope.push();
        if (push != Scope.ZERO) {
            if (isDebug) {
                logger.debug("push {}. skip trace. level:{} {}", new Object[]{scope.getName(), push, delegate.getClass()});
            }
            return;
        }
        this.delegate.before(target, className, methodName, parameterDescription, args);
    }

    @Override
    public void after(Object target, String className, String methodName, String parameterDescription, Object[] args, Object result, Throwable throwable) {
        final int pop = scope.pop();
        if (pop != Scope.ZERO) {
            if (isDebug) {
                logger.debug("pop {}. skip trace. level:{} {}", new Object[]{scope.getName(), pop, delegate.getClass()});
            }
            return;
        }
        this.delegate.after(target, className, methodName, parameterDescription, args, result, throwable);
    }


    @Override
    public void setTraceContext(TraceContext traceContext) {
        if (this.delegate instanceof TraceContextSupport) {
            ((TraceContextSupport) this.delegate).setTraceContext(traceContext);
        }
    }



}
