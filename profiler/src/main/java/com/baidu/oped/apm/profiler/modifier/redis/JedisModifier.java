
package com.baidu.oped.apm.profiler.modifier.redis;

import java.security.ProtectionDomain;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;
import com.baidu.oped.apm.bootstrap.instrument.NotFoundInstrumentException;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;
import com.baidu.oped.apm.profiler.modifier.redis.filter.JedisMethodNames;
import com.baidu.oped.apm.profiler.modifier.redis.filter.NameBasedMethodFilter;

/**
 * jedis(redis client) modifier
 * 
 * @author jaehong.kim
 *
 */
public class JedisModifier extends AbstractModifier {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JedisModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    @Override
    public String getTargetClass() {
        return "redis/clients/jedis/Jedis";
    }

    @Override
    public byte[] modify(ClassLoader classLoader, String className, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", className);
        }

        try {
            final InstrumentClass instrumentClass = byteCodeInstrumentor.getClass(classLoader, className, classFileBuffer);

            // before hook
            beforeAddInterceptor(classLoader, protectedDomain, instrumentClass);

            // add constructor
            addConstructorInterceptor(classLoader, protectedDomain, instrumentClass);

            // add method
            addMethodInterceptor(classLoader, protectedDomain, instrumentClass);

            return instrumentClass.toBytecode();
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to modifier. caused={}", e.getMessage(), e);
            }
        }

        return null;
    }

    protected void beforeAddInterceptor(ClassLoader classLoader, ProtectionDomain protectedDomain, final InstrumentClass instrumentClass) throws NotFoundInstrumentException, InstrumentException {
        // nothing
    }

    protected void addConstructorInterceptor(ClassLoader classLoader, ProtectionDomain protectedDomain, final InstrumentClass instrumentClass) throws InstrumentException, NotFoundInstrumentException {
        final Interceptor constructorInterceptor = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain, "com.baidu.oped.apm.profiler.modifier.redis.interceptor.JedisConstructorInterceptor");
        instrumentClass.addConstructorInterceptor(new String[] { "java.lang.String" }, constructorInterceptor);
        try {
            instrumentClass.addConstructorInterceptor(new String[] { "java.lang.String", "int" }, constructorInterceptor);
            instrumentClass.addConstructorInterceptor(new String[] { "java.lang.String", "int", "int" }, constructorInterceptor);
            instrumentClass.addConstructorInterceptor(new String[] { "java.net.URI" }, constructorInterceptor);
            instrumentClass.addConstructorInterceptor(new String[] { "redis.clients.jedis.JedisShardInfo" }, constructorInterceptor);
        } catch (Exception e) {
            // backward compatibility error
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to add constructor interceptor('not found redis.clients.jedis.JedisShardInfo' is compatibility error). caused={}", e.getMessage(), e);
            }
        }
    }

    protected void addMethodInterceptor(ClassLoader classLoader, ProtectionDomain protectedDomain, final InstrumentClass instrumentClass) throws NotFoundInstrumentException, InstrumentException {
        final List<MethodInfo> declaredMethods = instrumentClass.getDeclaredMethods(new NameBasedMethodFilter(JedisMethodNames.get()));
        for (MethodInfo method : declaredMethods) {
            final Interceptor methodInterceptor = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain, "com.baidu.oped.apm.profiler.modifier.redis.interceptor.JedisMethodInterceptor");
            instrumentClass.addInterceptor(method.getName(), method.getParameterTypes(), methodInterceptor);
        }
    }
}