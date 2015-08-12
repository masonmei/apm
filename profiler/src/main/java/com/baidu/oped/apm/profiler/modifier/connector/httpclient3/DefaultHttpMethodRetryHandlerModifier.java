
package com.baidu.oped.apm.profiler.modifier.connector.httpclient3;

import java.security.ProtectionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;

/**
 * class DefaultHttpMethodRetryHandlerModifier 
 *
 * @author meidongxu@baidu.com
 */
public class DefaultHttpMethodRetryHandlerModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public DefaultHttpMethodRetryHandlerModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    @Override
    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", javassistClassName);
        }

        try {
            Interceptor interceptor = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain, "com.baidu.oped.apm.profiler.modifier.connector.httpclient3.interceptor.RetryMethodInterceptor");

            InstrumentClass retryHandler = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);
            retryHandler.addInterceptor("retryMethod", new String[]{"org.apache.commons.httpclient.HttpMethod", "java.io.IOException", "int"}, interceptor);
            return retryHandler.toBytecode();
        } catch (Throwable e) {
            logger.warn("org.apache.commons.httpclient.DefaultHttpMethodRetryHandler modifier error. Caused:{}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String getTargetClass() {
        return "org/apache/commons/httpclient/DefaultHttpMethodRetryHandler";
    }
}
