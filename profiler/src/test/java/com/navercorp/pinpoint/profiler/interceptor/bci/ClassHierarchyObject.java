
package com.baidu.oped.apm.profiler.interceptor.bci;

import java.util.HashMap;

/**
 * class ClassHierarchyObject 
 *
 * @author meidongxu@baidu.com
 */
public class ClassHierarchyObject extends HashMap implements Runnable, Comparable {
    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public void run() {

    }
}
