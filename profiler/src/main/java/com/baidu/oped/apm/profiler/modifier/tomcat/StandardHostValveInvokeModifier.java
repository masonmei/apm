
package com.baidu.oped.apm.profiler.modifier.tomcat;

import java.security.ProtectionDomain;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modify org.apache.catalina.core.StandardHostValve class
 *
 * @author netspider
 * @author emeroad
 */
public class StandardHostValveInvokeModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public StandardHostValveInvokeModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    public String getTargetClass() {
        return "org/apache/catalina/core/StandardHostValve";
    }

    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", javassistClassName);
        }

        try {
            Interceptor interceptor = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain, "com.baidu.oped.apm.profiler.modifier.tomcat.interceptor.StandardHostValveInvokeInterceptor");

            InstrumentClass standardHostValve = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);
            standardHostValve.addInterceptor("invoke", new String[]{"org.apache.catalina.connector.Request", "org.apache.catalina.connector.Response"}, interceptor);
            return standardHostValve.toBytecode();
        } catch (InstrumentException e) {
            logger.warn("modify fail. Cause:" + e.getMessage(), e);
            return null;
        }
    }


}