
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
 * class BasicFutureModifier 
 *
 * @author meidongxu@baidu.com
 */
public class BasicFutureModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BasicFutureModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    @Override
    public String getTargetClass() {
        return "org/apache/http/concurrent/BasicFuture";
    }

    @Override
    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {} @ {}", javassistClassName, classLoader);
        }

        try {
            InstrumentClass aClass = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);

            Interceptor futureGetInterceptor = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain, "com.baidu.oped.apm.profiler.modifier.connector.httpclient4.interceptor.BasicFutureGetInterceptor");
            aClass.addInterceptor("get", null, futureGetInterceptor);

            Interceptor futureGetInterceptor2 = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain, "com.baidu.oped.apm.profiler.modifier.connector.httpclient4.interceptor.BasicFutureGetInterceptor");
            aClass.addInterceptor("get", new String[] { "long", "java.util.concurrent.TimeUnit" }, futureGetInterceptor2);

            Interceptor futureCompletedInterceptor  = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain, "com.baidu.oped.apm.profiler.modifier.connector.httpclient4.interceptor.BasicFutureCompletedInterceptor");
            aClass.addInterceptor("completed", new String[] { "java.lang.Object" }, futureCompletedInterceptor);

            Interceptor futureFailedInterceptor  = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain, "com.baidu.oped.apm.profiler.modifier.connector.httpclient4.interceptor.BasicFutureFailedInterceptor");
            aClass.addInterceptor("failed", new String[] { "java.lang.Exception" }, futureFailedInterceptor);

            return aClass.toBytecode();
        } catch (InstrumentException e) {
            logger.info("modify fail. Cause:{}", e.getMessage(), e);
            return null;
        }
    }
}
