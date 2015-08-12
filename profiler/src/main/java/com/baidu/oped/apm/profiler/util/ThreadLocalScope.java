
package com.baidu.oped.apm.profiler.util;

import com.baidu.oped.apm.bootstrap.instrument.Scope;

/**
 * class ThreadLocalScope 
 *
 * @author meidongxu@baidu.com
 */
public class ThreadLocalScope implements Scope {

    private final NamedThreadLocal<Scope> scope;


    public ThreadLocalScope(final ScopeFactory scopeFactory) {
        if (scopeFactory == null) {
            throw new NullPointerException("scopeFactory must not be null");
        }
        this.scope = new NamedThreadLocal<Scope>(scopeFactory.getName()) {
            @Override
            protected Scope initialValue() {
                return scopeFactory.createScope();
            }
        };
    }

    @Override
    public int push() {
        final Scope localScope = getLocalScope();
        return localScope.push();
    }

    @Override
    public int depth() {
        final Scope localScope = getLocalScope();
        return localScope.depth();
    }

    @Override
    public int pop() {
        final Scope localScope = getLocalScope();
        return localScope.pop();
    }

    protected Scope getLocalScope() {
        return scope.get();
    }


    @Override
    public String getName() {
        return scope.getName();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ThreadLocalScope{");
        sb.append("scope=").append(scope.getName());
        sb.append('}');
        return sb.toString();
    }
}