
package com.baidu.oped.apm.profiler.modifier.orm.mybatis;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;

/**
 * class DefaultSqlSessionModifier 
 *
 * @author meidongxu@baidu.com
 */
public class DefaultSqlSessionModifier extends MyBatisClientModifier {

    public static final String TARGET_CLASS_NAME = "org/apache/ibatis/session/defaults/DefaultSqlSession";

    public DefaultSqlSessionModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent, DefaultSqlSessionModifier.class);
    }

    @Override
    public String getTargetClass() {
        return TARGET_CLASS_NAME;
    }

}
