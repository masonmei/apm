
package com.baidu.oped.apm.profiler.modifier.redis;

import java.security.ProtectionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.MapTraceValue;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;

/**
 * jedis(redis client) Client modifier
 * - trace endPoint
 * 
 * @author jaehong.kim
 *
 */
public class JedisClientModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JedisClientModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    @Override
    public String getTargetClass() {
        return "redis/clients/jedis/Client";
    }

    @Override
    public byte[] modify(ClassLoader classLoader, String className, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", className);
        }

        try {
            final InstrumentClass instrumentClass = byteCodeInstrumentor.getClass(classLoader, className, classFileBuffer);

            // for trace endPoint
            instrumentClass.addTraceValue(MapTraceValue.class);
            
            final Interceptor constructorInterceptor = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain, "com.baidu.oped.apm.profiler.modifier.redis.interceptor.JedisClientConstructorInterceptor");
            instrumentClass.addConstructorInterceptor(new String[] { "java.lang.String" }, constructorInterceptor);
            instrumentClass.addConstructorInterceptor(new String[] { "java.lang.String", "int" }, constructorInterceptor);

            return instrumentClass.toBytecode();
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to modifier. caused={}", e.getMessage(), e);
            }
        }

        return null;
    }
}