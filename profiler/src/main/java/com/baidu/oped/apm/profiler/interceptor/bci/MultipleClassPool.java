
package com.baidu.oped.apm.profiler.interceptor.bci;

/**
 * class MultipleClassPool 
 *
 * @author meidongxu@baidu.com
 */
public interface MultipleClassPool {

    NamedClassPool getClassPool(ClassLoader classLoader);

}
