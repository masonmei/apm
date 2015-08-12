
package com.baidu.oped.apm.profiler.modifier.orm.mybatis;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;

/**
 * class SqlSessionTemplateModifier 
 *
 * @author meidongxu@baidu.com
 */
public class SqlSessionTemplateModifier extends MyBatisClientModifier {

    public static final String TARGET_CLASS_NAME = "org/mybatis/spring/SqlSessionTemplate";

    public SqlSessionTemplateModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent, SqlSessionTemplateModifier.class);
    }

    @Override
    public String getTargetClass() {
        return TARGET_CLASS_NAME;
    }



}
