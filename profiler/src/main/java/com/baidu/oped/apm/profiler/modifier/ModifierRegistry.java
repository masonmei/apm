
package com.baidu.oped.apm.profiler.modifier;

/**
 * class ModifierRegistry 
 *
 * @author meidongxu@baidu.com
 */
public interface ModifierRegistry {

    AbstractModifier findModifier(String className);

}
