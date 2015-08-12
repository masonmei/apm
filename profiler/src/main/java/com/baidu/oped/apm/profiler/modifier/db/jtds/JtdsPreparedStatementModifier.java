
package com.baidu.oped.apm.profiler.modifier.db.jtds;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.bootstrap.instrument.NotFoundInstrumentException;
import com.baidu.oped.apm.bootstrap.instrument.Scope;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.BindValueTraceValue;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.DatabaseInfoTraceValue;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.ParsingResultTraceValue;
import com.baidu.oped.apm.profiler.interceptor.ScopeDelegateStaticInterceptor;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;
import com.baidu.oped.apm.profiler.modifier.db.interceptor.PreparedStatementBindVariableInterceptor;
import com.baidu.oped.apm.profiler.modifier.db.interceptor.PreparedStatementExecuteQueryInterceptor;
import com.baidu.oped.apm.profiler.util.JavaAssistUtils;
import com.baidu.oped.apm.profiler.util.PreparedStatementUtils;

import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class JtdsPreparedStatementModifier 
 *
 * @author meidongxu@baidu.com
 */
public class JtdsPreparedStatementModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JtdsPreparedStatementModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    public String getTargetClass() {
        return "net/sourceforge/jtds/jdbc/JtdsPreparedStatement";
    }


    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", javassistClassName);
        }
        try {
            InstrumentClass preparedStatementClass = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);

            Interceptor executeInterceptor = new PreparedStatementExecuteQueryInterceptor();
            preparedStatementClass.addScopeInterceptor("execute", null, executeInterceptor, JtdsScope.SCOPE_NAME);

            Interceptor executeQueryInterceptor = new PreparedStatementExecuteQueryInterceptor();
            preparedStatementClass.addScopeInterceptor("executeQuery", null, executeQueryInterceptor, JtdsScope.SCOPE_NAME);

            Interceptor executeUpdateInterceptor = new PreparedStatementExecuteQueryInterceptor();
            preparedStatementClass.addScopeInterceptor("executeUpdate", null, executeUpdateInterceptor, JtdsScope.SCOPE_NAME);

            preparedStatementClass.addTraceValue(DatabaseInfoTraceValue.class);
            preparedStatementClass.addTraceValue(ParsingResultTraceValue.class);
            preparedStatementClass.addTraceValue(BindValueTraceValue.class, "new java.util.HashMap();");

            bindVariableIntercept(preparedStatementClass, classLoader, protectedDomain);

            return preparedStatementClass.toBytecode();
        } catch (InstrumentException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("{} modify fail. Cause:{}", this.getClass().getSimpleName(), e.getMessage(), e);
            }
            return null;
        }
    }

    private void bindVariableIntercept(InstrumentClass preparedStatement, ClassLoader classLoader, ProtectionDomain protectedDomain) throws InstrumentException {
        List<Method> bindMethod = PreparedStatementUtils.findBindVariableSetMethod();
        final Scope scope = byteCodeInstrumentor.getScope(JtdsScope.SCOPE_NAME);
        Interceptor interceptor = new ScopeDelegateStaticInterceptor(new PreparedStatementBindVariableInterceptor(), scope);
        int interceptorId = -1;
        for (Method method : bindMethod) {
            String methodName = method.getName();
            String[] parameterType = JavaAssistUtils.getParameterType(method.getParameterTypes());
            try {
                if (interceptorId == -1) {
                    interceptorId = preparedStatement.addInterceptor(methodName, parameterType, interceptor);
                } else {
                    preparedStatement.reuseInterceptor(methodName, parameterType, interceptorId);
                }
            } catch (NotFoundInstrumentException e) {
                // Cannot find bind variable setter method. This is not an error. logging will be enough.
                if (logger.isDebugEnabled()) {
                    logger.debug("bindVariable api not found. method:{} param:{} Cause:{}", methodName, Arrays.toString(parameterType), e.getMessage());
                }
            }
        }
    }




}
