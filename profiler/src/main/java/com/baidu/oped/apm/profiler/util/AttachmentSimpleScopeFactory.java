
package com.baidu.oped.apm.profiler.util;

import com.baidu.oped.apm.bootstrap.instrument.AttachmentFactory;
import com.baidu.oped.apm.bootstrap.instrument.AttachmentScope;
import com.baidu.oped.apm.bootstrap.instrument.Scope;

/**
 * class AttachmentSimpleScopeFactory 
 *
 * @author meidongxu@baidu.com
 */
public class AttachmentSimpleScopeFactory<T> implements ScopeFactory {

    private final String name;

    public AttachmentSimpleScopeFactory(String name) {
        this.name = name;
    }

    @Override
    public Scope createScope() {
        return new AttachmentSimpleScope(name);
    }

    @Override
    public String getName() {
        return name;
    }
}
