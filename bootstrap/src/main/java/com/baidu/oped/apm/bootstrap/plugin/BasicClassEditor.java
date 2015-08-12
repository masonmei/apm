package com.baidu.oped.apm.bootstrap.plugin;

import java.util.List;

import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.exception.PinpointException;

/**
 * class BasicClassEditor
 *
 * @author meidongxu@baidu.com
 */
public class BasicClassEditor implements DedicatedClassEditor {
    private final String targetClassName;
    private final List<MetadataInjector> metadataInjectors;
    private final List<InterceptorInjector> interceptorInjectors;

    public BasicClassEditor(String targetClassName, List<MetadataInjector> metadataInjectors,
                            List<InterceptorInjector> interceptorInjectors) {
        this.targetClassName = targetClassName;
        this.metadataInjectors = metadataInjectors;
        this.interceptorInjectors = interceptorInjectors;
    }

    @Override
    public byte[] edit(ClassLoader classLoader, InstrumentClass target) {
        try {
            for (MetadataInjector injector : metadataInjectors) {
                injector.inject(classLoader, target);
            }

            for (InterceptorInjector injector : interceptorInjectors) {
                injector.inject(classLoader, target);
            }

            return target.toBytecode();
        } catch (Throwable t) {
            throw new PinpointException("Fail to edit class: " + targetClassName, t);
        }
    }

    @Override
    public String getTargetClassName() {
        return targetClassName;
    }

}
