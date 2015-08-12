
package com.baidu.oped.apm.thrift.io;

/**
 * class SerializerFactory 
 *
 * @author meidongxu@baidu.com
 */
public interface SerializerFactory<E> {
    E createSerializer();
}
