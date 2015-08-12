package com.baidu.oped.apm.bootstrap.plugin;

import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;

/**
 * class InterceptorFactory
 *
 * @author meidongxu@baidu.com
 */
public interface InterceptorFactory {
    Interceptor getInterceptor(ClassLoader classLoader, InstrumentClass target, MethodInfo targetMethod);
}
