package com.baidu.oped.apm.bootstrap.plugin;

import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;

/**
 * class ConstructorInterceptorInjector
 *
 * @author meidongxu@baidu.com
 */
public class ConstructorInterceptorInjector implements InterceptorInjector {
    private final String[] targetParameterTypes;
    private final InterceptorFactory factory;

    public ConstructorInterceptorInjector(String[] targetParameterTypes, InterceptorFactory factory) {
        this.targetParameterTypes = targetParameterTypes;
        this.factory = factory;
    }

    @Override
    public void inject(ClassLoader classLoader, InstrumentClass target) throws InstrumentException {
        MethodInfo targetMethod = target.getConstructor(targetParameterTypes);
        Interceptor interceptor = factory.getInterceptor(classLoader, target, targetMethod);
        target.addConstructorInterceptor(targetParameterTypes, interceptor);
    }
}
