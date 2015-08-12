package com.baidu.oped.apm.bootstrap.plugin;

import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;

/**
 * class ConditionalInterceptorInjector
 *
 * @author meidongxu@baidu.com
 */
public class ConditionalInterceptorInjector implements InterceptorInjector {
    private final Condition condition;
    private final InterceptorInjector delegate;

    public ConditionalInterceptorInjector(Condition condition, InterceptorInjector delegate) {
        this.condition = condition;
        this.delegate = delegate;
    }

    @Override
    public void inject(ClassLoader classLoader, InstrumentClass target) throws InstrumentException {
        if (condition.check(target)) {
            delegate.inject(classLoader, target);
        }
    }
}
