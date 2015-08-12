
package com.baidu.oped.apm.profiler.interceptor;

import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.instrument.Scope;
import com.baidu.oped.apm.bootstrap.interceptor.StaticAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.TraceContextSupport;

/**
 * class ScopeDelegateStaticInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class ScopeDelegateStaticInterceptor implements StaticAroundInterceptor, TraceContextSupport {
    private final StaticAroundInterceptor delegate;
    private final Scope scope;


    public ScopeDelegateStaticInterceptor(StaticAroundInterceptor delegate, Scope scope) {
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
            return;
        }
        this.delegate.before(target, className, methodName, parameterDescription, args);
    }

    @Override
    public void after(Object target, String className, String methodName, String parameterDescription, Object[] args, Object result, Throwable throwable) {
        final int pop = scope.pop();
        if (pop != Scope.ZERO) {
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
