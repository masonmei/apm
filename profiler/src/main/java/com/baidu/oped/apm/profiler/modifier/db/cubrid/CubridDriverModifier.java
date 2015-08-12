
package com.baidu.oped.apm.profiler.modifier.db.cubrid;

import java.security.ProtectionDomain;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.bootstrap.instrument.Scope;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;
import com.baidu.oped.apm.profiler.modifier.db.interceptor.DriverConnectInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class CubridDriverModifier 
 *
 * @author meidongxu@baidu.com
 */
public class CubridDriverModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CubridDriverModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    public String getTargetClass() {
        return "cubrid/jdbc/driver/CUBRIDDriver";
    }

    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", javassistClassName);
        }
        try {
            InstrumentClass mysqlConnection = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);

            final Scope scope = byteCodeInstrumentor.getScope(CubridScope.SCOPE_NAME);
            DriverConnectInterceptor driverConnectInterceptor = new DriverConnectInterceptor(scope);
            mysqlConnection.addInterceptor("connect", new String[]{"java.lang.String", "java.util.Properties"}, driverConnectInterceptor);

            if (this.logger.isInfoEnabled()) {
                this.logger.info("{} class is converted.", javassistClassName);
            }

            return mysqlConnection.toBytecode();
        } catch (InstrumentException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("{} modify fail. Cause:{}", this.getClass().getSimpleName(), e.getMessage(), e);
            }
            return null;
        }
    }
}
