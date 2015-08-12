
package com.baidu.oped.apm.profiler.util;

import com.baidu.oped.apm.bootstrap.instrument.Scope;
import com.baidu.oped.apm.bootstrap.instrument.ScopeDefinition;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * class ThreadLocalScopePool 
 *
 * @author meidongxu@baidu.com
 */
public class ThreadLocalScopePool implements ScopePool {

    private final ConcurrentMap<ScopeDefinition, Scope> pool = new ConcurrentHashMap<ScopeDefinition, Scope>();

    @Override
    public Scope getScope(ScopeDefinition scopeDefinition) {
        if (scopeDefinition == null) {
            throw new NullPointerException("scopeDefinition must not be null");
        }
        final Scope scope = this.pool.get(scopeDefinition);
        if (scope != null) {
            return scope;
        }

        final Scope newScope = createScope(scopeDefinition);
        final Scope exist = this.pool.putIfAbsent(scopeDefinition, newScope);
        if (exist != null) {
            return exist;
        }
        return newScope;
    }

    private Scope createScope(ScopeDefinition scopeDefinition) {

        if (scopeDefinition.getType() == ScopeDefinition.Type.ATTACHMENT) {

            AttachmentSimpleScopeFactory<Object> factory = new AttachmentSimpleScopeFactory<Object>(scopeDefinition.getName());
            return new AttachmentThreadLocalScope<Object>(factory);

        } else if (scopeDefinition.getType() == ScopeDefinition.Type.SIMPLE) {

            SimpleScopeFactory simpleScopeFactory = new SimpleScopeFactory(scopeDefinition.getName());
            return new ThreadLocalScope(simpleScopeFactory);

        } else {
            throw new UnsupportedOperationException(scopeDefinition.getType() + " type unsupported.");
        }

    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScopePool{");
        sb.append("pool=").append(pool);
        sb.append('}');
        return sb.toString();
    }
}
