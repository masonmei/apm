
package com.baidu.oped.apm.profiler.modifier.orm.ibatis;

import java.security.ProtectionDomain;
import java.util.List;

import org.slf4j.Logger;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.MethodFilter;
import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;
import com.baidu.oped.apm.profiler.modifier.orm.ibatis.interceptor.IbatisScope;
import com.baidu.oped.apm.profiler.modifier.orm.ibatis.interceptor.IbatisSqlMapOperationInterceptor;

/**
 * class IbatisClientModifier 
 *
 * @author meidongxu@baidu.com
 */
/**
 * Base class for modifying iBatis client classes
 *  
 * @author Hyun Jeong
 */
public abstract class IbatisClientModifier extends AbstractModifier {

    private static final ServiceType serviceType = ServiceType.IBATIS;
    private static final String SCOPE = IbatisScope.SCOPE;

    protected Logger logger;

    protected abstract MethodFilter getIbatisApiMethodFilter();

    public IbatisClientModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    @Override
    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifying. {}", javassistClassName);
        }
        try {
            InstrumentClass ibatisClientImpl = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);
            List<MethodInfo> declaredMethods = ibatisClientImpl.getDeclaredMethods(getIbatisApiMethodFilter());

            for (MethodInfo method : declaredMethods) {
                Interceptor ibatisApiInterceptor = new IbatisSqlMapOperationInterceptor(serviceType);
                ibatisClientImpl.addScopeInterceptor(method.getName(), method.getParameterTypes(), ibatisApiInterceptor, SCOPE);
            }

            return ibatisClientImpl.toBytecode();
        } catch (Throwable e) {
            this.logger.warn("{} modifier error. Cause:{}", javassistClassName, e.getMessage(), e);
            return null;
        }
    }
}
