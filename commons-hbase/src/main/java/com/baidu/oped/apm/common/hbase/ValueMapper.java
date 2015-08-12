
package com.baidu.oped.apm.common.hbase;

/**
 * class ValueMapper 
 *
 * @author meidongxu@baidu.com
 */
public interface ValueMapper<T> {
    byte[] mapValue(T value);
}
