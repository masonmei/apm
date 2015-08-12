
package com.baidu.oped.apm.profiler.modifier.redis;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;

/**
 * jedis(redis client) pipeline modifier
 * 
 * @author jaehong.kim
 *
 */
public class JedisMultiKeyPipelineBaseModifier extends JedisPipelineBaseModifier {

    public JedisMultiKeyPipelineBaseModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    @Override
    public String getTargetClass() {
        return "redis/clients/jedis/MultiKeyPipelineBase";
    }
}