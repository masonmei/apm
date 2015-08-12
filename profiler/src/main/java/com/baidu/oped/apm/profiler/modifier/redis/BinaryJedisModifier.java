
package com.baidu.oped.apm.profiler.modifier.redis;

import java.security.ProtectionDomain;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;
import com.baidu.oped.apm.bootstrap.instrument.InstrumentException;
import com.baidu.oped.apm.bootstrap.instrument.NotFoundInstrumentException;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.MapTraceValue;

/**
 * jedis(redis client) modifier
 * 
 * @author jaehong.kim
 *
 */
public class BinaryJedisModifier extends JedisModifier {

    public BinaryJedisModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    @Override
    public String getTargetClass() {
        return "redis/clients/jedis/BinaryJedis";
    }

    @Override
    protected void beforeAddInterceptor(ClassLoader classLoader, ProtectionDomain protectedDomain, final InstrumentClass instrumentClass) throws NotFoundInstrumentException, InstrumentException {
        // for trace endPoint. 
        instrumentClass.addTraceValue(MapTraceValue.class);
    }
}
