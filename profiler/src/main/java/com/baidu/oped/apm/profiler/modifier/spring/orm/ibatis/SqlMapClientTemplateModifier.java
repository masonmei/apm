
package com.baidu.oped.apm.profiler.modifier.spring.orm.ibatis;

import java.security.ProtectionDomain;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.MethodFilter;
import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;
import com.baidu.oped.apm.profiler.modifier.orm.ibatis.filter.SqlMapClientMethodFilter;
import com.baidu.oped.apm.profiler.modifier.orm.ibatis.interceptor.IbatisScope;
import com.baidu.oped.apm.profiler.modifier.orm.ibatis.interceptor.IbatisSqlMapOperationInterceptor;

/**
 * SqlMapClientTemplate Modifier
 * <p/>
 * Hooks onto <i>org.springframework.orm.ibatis.SqlMapClientTemplate</i>
 * <p/>
 * 
 * @author Hyun Jeong
 * @see com.ibatis.sqlmap.client.SqlMapExecutor
 */
public final class SqlMapClientTemplateModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final ServiceType serviceType = ServiceType.SPRING_ORM_IBATIS;
    private static final String SCOPE = IbatisScope.SCOPE;
    private static final MethodFilter sqlMapClientMethodFilter = new SqlMapClientMethodFilter();

    public static final String TARGET_CLASS_NAME = "org/springframework/orm/ibatis/SqlMapClientTemplate";

    public SqlMapClientTemplateModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    @Override
    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Modifying. {}", javassistClassName);
        }
        try {
            InstrumentClass sqlMapClientTemplate = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);
            List<MethodInfo> declaredMethods = sqlMapClientTemplate.getDeclaredMethods(sqlMapClientMethodFilter);

            for (MethodInfo method : declaredMethods) {
                Interceptor sqlMapClientTemplateInterceptor = new IbatisSqlMapOperationInterceptor(serviceType);
                sqlMapClientTemplate.addScopeInterceptor(method.getName(), method.getParameterTypes(), sqlMapClientTemplateInterceptor, SCOPE);
            }

            return sqlMapClientTemplate.toBytecode();
        } catch (Throwable e) {
            this.logger.warn("{} modifier error. Cause:{}", javassistClassName, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String getTargetClass() {
        return TARGET_CLASS_NAME;
    }

}
