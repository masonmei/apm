
package com.baidu.oped.apm.profiler.modifier.arcus;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;

import org.slf4j.LoggerFactory;

/**
 * class OperationFutureModifier 
 *
 * @author meidongxu@baidu.com
 */
public class OperationFutureModifier extends AbstractFutureModifier {


    public OperationFutureModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public String getTargetClass() {
        return "net/spy/memcached/internal/OperationFuture";
    }
}
