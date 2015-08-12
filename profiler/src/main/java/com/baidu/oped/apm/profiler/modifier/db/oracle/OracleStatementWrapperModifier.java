
package com.baidu.oped.apm.profiler.modifier.db.oracle;

import java.security.ProtectionDomain;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;
import com.baidu.oped.apm.profiler.modifier.ModifierDelegate;

/**
 * class OracleStatementWrapperModifier 
 *
 * @author meidongxu@baidu.com
 */
public class OracleStatementWrapperModifier extends AbstractModifier {

    private final ModifierDelegate delegate;

    public OracleStatementWrapperModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
        this.delegate = new OracleStatementModifierDelegate(byteCodeInstrumentor);
    }

    @Override
    public String getTargetClass() {
        return OracleClassConstants.ORACLE_STATEMENT_WRAPPER;
    }

    @Override
    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        return this.delegate.modify(classLoader, javassistClassName, protectedDomain, classFileBuffer);
    }

}
