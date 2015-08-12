
package com.baidu.oped.apm.profiler.modifier.tomcat;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.ProtectionDomain;

/**
 * class RequestFacadeModifier 
 *
 * @author meidongxu@baidu.com
 */
public class RequestFacadeModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RequestFacadeModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    public String getTargetClass() {
        return "org/apache/catalina/connector/RequestFacade";
    }

    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", javassistClassName);
        }


        try {
            InstrumentClass requestFacade = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);
            requestFacade.weaving("com.baidu.oped.apm.profiler.modifier.tomcat.aspect.RequestFacadeAspect", classLoader);
            return requestFacade.toBytecode();
        } catch (InstrumentException e) {
            logger.warn("modify fail. Cause:" + e.getMessage(), e);
            return null;
        }
    }
}