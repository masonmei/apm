
package com.baidu.oped.apm.collector.util;

/**
 * class ObjectPoolFactory 
 *
 * @author meidongxu@baidu.com
 */
public interface ObjectPoolFactory<T> {
    T create();

    void beforeReturn(T t);
}
