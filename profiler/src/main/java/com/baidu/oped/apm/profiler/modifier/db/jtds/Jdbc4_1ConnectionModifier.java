
package com.baidu.oped.apm.profiler.modifier.db.jtds;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;

/**
 * 1.3.x -> jdk 1.7
 * @author emeroad
 */
public class Jdbc4_1ConnectionModifier extends JtdsConnectionModifier {

    public Jdbc4_1ConnectionModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    public String getTargetClass() {
        return "net/sourceforge/jtds/jdbc/JtdsConnection";
    }
}
