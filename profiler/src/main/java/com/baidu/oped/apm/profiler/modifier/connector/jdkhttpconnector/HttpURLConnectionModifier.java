
package com.baidu.oped.apm.profiler.modifier.connector.jdkhttpconnector;

import java.security.ProtectionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;
import com.baidu.oped.apm.profiler.modifier.connector.jdkhttpconnector.interceptor.ConnectMethodInterceptor;

/**
 * class HttpURLConnectionModifier 
 *
 * @author meidongxu@baidu.com
 */
/**
 * TODO Fix class loader issue.
 * @author netspider
 * 
 */
public class HttpURLConnectionModifier extends AbstractModifier {
    private final static String SCOPE = "HttpURLConnectoin";
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HttpURLConnectionModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    public String getTargetClass() {
        return "sun/net/www/protocol/http/HttpURLConnection";
    }

    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", javassistClassName);
        }

        try {
            InstrumentClass aClass = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);
            
            aClass.addGetter("__isConnected", "connected", "boolean");
            
            boolean hasConnecting;
            try {
                aClass.addGetter("__isConnecting", "connecting", "boolean");
                hasConnecting = true;
            } catch (InstrumentException e) {
                hasConnecting = false;
            }

            ConnectMethodInterceptor connectMethodInterceptor = new ConnectMethodInterceptor(hasConnecting);
            aClass.addScopeInterceptor("connect", null, connectMethodInterceptor, SCOPE);
            
            ConnectMethodInterceptor getInputStreamInterceptor = new ConnectMethodInterceptor(hasConnecting);
            aClass.addScopeInterceptor("getInputStream", null, getInputStreamInterceptor, SCOPE);
            
            ConnectMethodInterceptor getOutputStreamInterceptor = new ConnectMethodInterceptor(hasConnecting);
            aClass.addScopeInterceptor("getOutputStream", null, getOutputStreamInterceptor, SCOPE);
            
            
            return aClass.toBytecode();
        } catch (InstrumentException e) {
            logger.warn("HttpURLConnectionModifier fail. Caused:", e.getMessage(), e);
            return null;
        }
    }
}