
package com.baidu.oped.apm.profiler.modifier.arcus;

import java.security.ProtectionDomain;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class BaseOperationModifier 
 *
 * @author meidongxu@baidu.com
 */
public class BaseOperationModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BaseOperationModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    public String getTargetClass() {
        return "net/spy/memcached/protocol/BaseOperationImpl";
    }

    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", javassistClassName);
        }

        try {
            InstrumentClass aClass = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);

            aClass.addTraceVariable("__serviceCode", "__setServiceCode", "__getServiceCode", "java.lang.String");

            /* Do not intercept
            aClass.addTraceVariable("__asyncTrace", "__setAsyncTrace", "__getAsyncTrace", "java.lang.Object");

            aClass.addConstructorInterceptor(null, new BaseOperationConstructInterceptor());

            Interceptor transitionStateInterceptor = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain, "com.baidu.oped.apm.profiler.modifier.arcus.interceptor.BaseOperationTransitionStateInterceptor");
            aClass.addInterceptor("transitionState", new String[]{"net.spy.memcached.ops.OperationState"}, transitionStateInterceptor, Type.before);

            Interceptor cancelInterceptor = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain, "com.baidu.oped.apm.profiler.modifier.arcus.interceptor.BaseOperationCancelInterceptor");
            aClass.addInterceptor("cancel", null, cancelInterceptor, Type.after);
            */

            return aClass.toBytecode();
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getMessage(), e);
            }
            return null;
        }
    }
}