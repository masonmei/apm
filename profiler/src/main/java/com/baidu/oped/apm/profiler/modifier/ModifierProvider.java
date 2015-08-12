
package com.baidu.oped.apm.profiler.modifier;

import java.util.List;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.bootstrap.plugin.ProfilerPlugin;

/**
 * class ModifierProvider 
 *
 * @author meidongxu@baidu.com
 */
/**
 * ModifierProvider is a temporary interface to provide additional modifiers to Pinpoint profiler.
 * This will be replaced by {@link ProfilerPlugin} later.
 * 
 * @deprecated
 * @author lioolli
 */
@Deprecated
public interface ModifierProvider {
    List<Modifier> getModifiers(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent);
}
