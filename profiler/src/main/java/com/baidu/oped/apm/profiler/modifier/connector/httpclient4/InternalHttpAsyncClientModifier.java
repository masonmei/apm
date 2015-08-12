
package com.baidu.oped.apm.profiler.modifier.connector.httpclient4;

import java.security.ProtectionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;

/**
 * class InternalHttpAsyncClientModifier 
 *
 * @author meidongxu@baidu.com
 */
@Deprecated
public class InternalHttpAsyncClientModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public InternalHttpAsyncClientModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    @Override
    public String getTargetClass() {
        return "org/apache/http/impl/nio/client/InternalHttpAsyncClient";
    }

    @Override
    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {} @ {}", javassistClassName, classLoader);
        }

        try {
            InstrumentClass aClass = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);

            Interceptor internalExecuteInterceptor = byteCodeInstrumentor.newInterceptor(classLoader,
                    protectedDomain,
                    "com.baidu.oped.apm.profiler.modifier.connector.httpclient4.interceptor.AsyncInternalClientExecuteInterceptor");

            String[] internalExecuteParams = new String[] {
                    "org.apache.http.nio.protocol.HttpAsyncRequestProducer",
                    "org.apache.http.nio.protocol.HttpAsyncResponseConsumer",
                    "org.apache.http.protocol.HttpContext",
                    "org.apache.http.concurrent.FutureCallback"
                    };
            aClass.addInterceptor("execute", internalExecuteParams, internalExecuteInterceptor);

            return aClass.toBytecode();
        } catch (InstrumentException e) {
            logger.info("modify fail. Cause:{}", e.getMessage(), e);
            return null;
        }
    }
}
