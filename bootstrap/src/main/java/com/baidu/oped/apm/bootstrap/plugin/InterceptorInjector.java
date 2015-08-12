package com.baidu.oped.apm.bootstrap.plugin;

import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;

/**
 * class InterceptorInjector
 *
 * @author meidongxu@baidu.com
 */
public interface InterceptorInjector {
    void inject(ClassLoader classLoader, InstrumentClass target) throws InstrumentException;
}
