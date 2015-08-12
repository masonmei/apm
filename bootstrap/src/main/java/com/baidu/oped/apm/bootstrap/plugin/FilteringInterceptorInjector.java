package com.baidu.oped.apm.bootstrap.plugin;

import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.bootstrap.instrument.MethodFilter;
import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;

/**
 * class FilteringInterceptorInjector
 *
 * @author meidongxu@baidu.com
 */
public class FilteringInterceptorInjector implements InterceptorInjector {
    private final MethodFilter filter;
    private final InterceptorFactory factory;
    private final boolean singletonInterceptor;

    public FilteringInterceptorInjector(MethodFilter filter, InterceptorFactory factory, boolean singletonInterceptor) {
        this.filter = filter;
        this.factory = factory;
        this.singletonInterceptor = singletonInterceptor;
    }

    @Override
    public void inject(ClassLoader classLoader, InstrumentClass target) throws InstrumentException {
        int interceptorId = -1;

        for (MethodInfo methodInfo : target.getDeclaredMethods(filter)) {
            String targetMethodName = methodInfo.getName();
            String[] targetParameterTypes = methodInfo.getParameterTypes();

            if (singletonInterceptor && interceptorId != -1) {
                target.reuseInterceptor(targetMethodName, targetParameterTypes, interceptorId);
            } else {
                Interceptor interceptor = factory.getInterceptor(classLoader, target, methodInfo);
                interceptorId = target.addInterceptor(targetMethodName, targetParameterTypes, interceptor);
            }
        }
    }
}
