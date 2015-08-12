package com.baidu.oped.apm.bootstrap.plugin;

import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;

/**
 * class DedicatedInterceptorInjector
 *
 * @author meidongxu@baidu.com
 */
public class DedicatedInterceptorInjector implements InterceptorInjector {
    private final String targetMethodName;
    private final String[] targetMethodParameterTypes;
    private final InterceptorFactory factory;

    public DedicatedInterceptorInjector(String targetMethodName, String[] targetMethodParameterTypes,
                                        InterceptorFactory factory) {
        this.targetMethodName = targetMethodName;
        this.targetMethodParameterTypes = targetMethodParameterTypes;
        this.factory = factory;
    }

    @Override
    public void inject(ClassLoader classLoader, InstrumentClass target) throws InstrumentException {
        MethodInfo targetMethod = target.getDeclaredMethod(targetMethodName, targetMethodParameterTypes);
        Interceptor interceptor = factory.getInterceptor(classLoader, target, targetMethod);
        target.addInterceptor(targetMethodName, targetMethodParameterTypes, interceptor);
    }

}
