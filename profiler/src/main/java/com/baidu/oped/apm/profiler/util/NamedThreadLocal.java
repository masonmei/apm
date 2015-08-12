
package com.baidu.oped.apm.profiler.util;

/**
 * NamedThreadLocal makes thread local leak trace easy.
 *
 * @author emeroad
 */
public class NamedThreadLocal<T> extends ThreadLocal<T> {
    private final String name;

    public NamedThreadLocal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
