
package com.baidu.oped.apm.profiler.util;

import com.baidu.oped.apm.bootstrap.instrument.Scope;

/**
 * class SimpleScopeFactory 
 *
 * @author meidongxu@baidu.com
 */
public class SimpleScopeFactory implements ScopeFactory {

    private final String name;

    public SimpleScopeFactory(String name) {
        this.name = name;
    }

    @Override
    public Scope createScope() {
        return new SimpleScope(name);
    }

    @Override
    public String getName() {
        return name;
    }
}
