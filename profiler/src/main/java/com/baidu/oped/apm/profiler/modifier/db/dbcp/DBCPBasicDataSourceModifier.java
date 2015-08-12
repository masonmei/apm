
package com.baidu.oped.apm.profiler.modifier.db.dbcp;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;
import com.baidu.oped.apm.profiler.modifier.db.interceptor.DataSourceGetConnectionInterceptor;

import java.security.ProtectionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class DBCPBasicDataSourceModifier 
 *
 * @author meidongxu@baidu.com
 */
public class DBCPBasicDataSourceModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DBCPBasicDataSourceModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    public String getTargetClass() {
        return "org/apache/commons/dbcp/BasicDataSource";
    }

    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", javassistClassName);
        }

        try {
            InstrumentClass basicDataSource = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);
            Interceptor getConnection0 = new DataSourceGetConnectionInterceptor();
            basicDataSource.addScopeInterceptor("getConnection", null, getConnection0, DBCPScope.SCOPE_NAME);

            Interceptor getConnection1 = new DataSourceGetConnectionInterceptor();
            basicDataSource.addScopeInterceptor("getConnection", new String[] {"java.lang.String", "java.lang.String"}, getConnection1, DBCPScope.SCOPE_NAME);

            return basicDataSource.toBytecode();
        } catch (InstrumentException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("{} modify fail. Cause:{}", this.getClass().getSimpleName(), e.getMessage(), e);
            }
            return null;
        }
    }


}
