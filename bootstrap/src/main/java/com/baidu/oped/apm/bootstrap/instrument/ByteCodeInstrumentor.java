package com.baidu.oped.apm.bootstrap.instrument;

import java.security.ProtectionDomain;

import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;

/**
 * class ByteCodeInstrumentor
 *
 * @author meidongxu@baidu.com
 */
public interface ByteCodeInstrumentor {

    InstrumentClass getClass(ClassLoader classLoader, String jvmClassName, byte[] classFileBuffer)
            throws InstrumentException;

    boolean findClass(ClassLoader classLoader, String javassistClassName);

    Scope getScope(String scopeName);

    Scope getScope(ScopeDefinition scopeDefinition);

    Class<?> defineClass(ClassLoader classLoader, String defineClass, ProtectionDomain protectedDomain)
            throws InstrumentException;

    Interceptor newInterceptor(ClassLoader classLoader, ProtectionDomain protectedDomain, String interceptorFQCN)
            throws InstrumentException;

    //    Interceptor newInterceptor(ClassLoader classLoader, ProtectionDomain protectedDomain, String
    // interceptorFQCN, Object[] params) throws InstrumentException;

    Interceptor newInterceptor(ClassLoader classLoader, ProtectionDomain protectedDomain, String interceptorFQCN,
                               Object[] params, Class[] paramClazz) throws InstrumentException;
}
