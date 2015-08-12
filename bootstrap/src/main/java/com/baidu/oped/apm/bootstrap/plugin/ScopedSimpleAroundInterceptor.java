package com.baidu.oped.apm.bootstrap.plugin;

import com.baidu.oped.apm.bootstrap.instrument.Scope;
import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;

/**
 * class ScopedSimpleAroundInterceptor
 *
 * @author meidongxu@baidu.com
 */
public class ScopedSimpleAroundInterceptor implements SimpleAroundInterceptor {

    private final SimpleAroundInterceptor delegate;
    private final Scope scope;

    public ScopedSimpleAroundInterceptor(SimpleAroundInterceptor delegate, Scope scope) {
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

        if (push != 0) {
            return;
        }

        this.delegate.before(target, args);
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        final int pop = scope.pop();

        if (pop != 0) {
            return;
        }

        this.delegate.after(target, args, result, throwable);
    }
}
