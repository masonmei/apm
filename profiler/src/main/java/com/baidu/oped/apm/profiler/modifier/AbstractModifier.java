
package com.baidu.oped.apm.profiler.modifier;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;

/**
 * class AbstractModifier 
 *
 * @author meidongxu@baidu.com
 */
public abstract class AbstractModifier implements Modifier {

    protected final ByteCodeInstrumentor byteCodeInstrumentor;
    protected final Agent agent;

    public AbstractModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        if (byteCodeInstrumentor == null) {
            throw new NullPointerException("byteCodeInstrumentor must not be null");
        }
        if (agent == null) {
            throw new NullPointerException("agent must not be null");
        }
        this.byteCodeInstrumentor = byteCodeInstrumentor;
        this.agent = agent;
    }

    public Agent getAgent() {
        return agent;
    }
    
    public abstract String getTargetClass();
}
