
package com.baidu.oped.apm.profiler.modifier.connector.httpclient4;

import java.security.ProtectionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;
import com.baidu.oped.apm.profiler.modifier.connector.httpclient4.interceptor.RetryRequestInterceptor;

/**
 * class DefaultHttpRequestRetryHandlerModifier 
 *
 * @author meidongxu@baidu.com
 */
public class DefaultHttpRequestRetryHandlerModifier extends AbstractModifier {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultHttpRequestRetryHandlerModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    @Override
    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", javassistClassName);
        }
        
        RetryRequestInterceptor retryRequestInterceptor = new RetryRequestInterceptor();
        
        try {
            InstrumentClass httpRequestRetryHandler = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);
            httpRequestRetryHandler.addInterceptor("retryRequest", new String[]{"java.io.IOException", "int", "org.apache.http.protocol.HttpContext"}, retryRequestInterceptor);

            return httpRequestRetryHandler.toBytecode();
        } catch (Throwable e) {
            logger.warn("DefaultHttpRequestRetryHandler modifier error. Caused:{}", e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public String getTargetClass() {
        return "org/apache/http/impl/client/DefaultHttpRequestRetryHandler";
    }
    
}