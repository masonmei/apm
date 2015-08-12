
package com.baidu.oped.apm.profiler.modifier.db.oracle;

import java.security.ProtectionDomain;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;
import com.baidu.oped.apm.profiler.modifier.ModifierDelegate;

/**
 * class OraclePreparedStatementWrapperModifier 
 *
 * @author meidongxu@baidu.com
 */
public class OraclePreparedStatementWrapperModifier extends AbstractModifier {

    private final ModifierDelegate delegate;

    public OraclePreparedStatementWrapperModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
        this.delegate = new OraclePreparedStatementModifierDelegate(byteCodeInstrumentor);
    }

    public String getTargetClass() {
        return OracleClassConstants.ORACLE_PREPARED_STATEMENT_WRAPPER;
    }

    @Override
    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        return this.delegate.modify(classLoader, javassistClassName, protectedDomain, classFileBuffer);
    }

}
