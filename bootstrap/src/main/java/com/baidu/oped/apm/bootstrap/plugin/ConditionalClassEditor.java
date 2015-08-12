package com.baidu.oped.apm.bootstrap.plugin;

import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;

/**
 * class ConditionalClassEditor
 *
 * @author meidongxu@baidu.com
 */
public class ConditionalClassEditor implements DedicatedClassEditor {
    private final Condition condition;
    private final DedicatedClassEditor delegate;

    public ConditionalClassEditor(Condition condition, DedicatedClassEditor delegate) {
        this.condition = condition;
        this.delegate = delegate;
    }

    @Override
    public byte[] edit(ClassLoader classLoader, InstrumentClass target) {
        if (condition.check(target)) {
            return delegate.edit(classLoader, target);
        }

        return null;
    }

    @Override
    public String getTargetClassName() {
        return delegate.getTargetClassName();
    }
}
