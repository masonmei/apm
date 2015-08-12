
package com.baidu.oped.apm.profiler.interceptor.bci;

import javassist.ClassPool;

/**
 * class NamedClassPool 
 *
 * @author meidongxu@baidu.com
 */
public class NamedClassPool extends ClassPool {
    private final String name;

    public NamedClassPool(String name) {
        this.name = name;
    }

    public NamedClassPool(boolean useDefaultPath, String name) {
        super(useDefaultPath);
        this.name = name;
    }

    public NamedClassPool(ClassPool parent, String name) {
        super(parent);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
