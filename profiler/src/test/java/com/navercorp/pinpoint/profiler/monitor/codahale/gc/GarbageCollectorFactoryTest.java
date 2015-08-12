
package com.baidu.oped.apm.profiler.monitor.codahale.gc;

import com.baidu.oped.apm.profiler.monitor.codahale.AgentStatCollectorFactory;
import com.baidu.oped.apm.profiler.monitor.codahale.gc.GarbageCollector;
import com.baidu.oped.apm.thrift.dto.TJvmGc;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class GarbageCollectorFactoryTest 
 *
 * @author meidongxu@baidu.com
 */
public class GarbageCollectorFactoryTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test() {
        GarbageCollector collector = new AgentStatCollectorFactory().getGarbageCollector();

        logger.debug("collector.getType():{}", collector);
        TJvmGc collect1 = collector.collect();
        logger.debug("collector.collect():{}", collect1);
        TJvmGc collect2 = collector.collect();
        logger.debug("collector.collect():{}", collect2);
    }
}
