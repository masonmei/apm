
package com.baidu.oped.apm.thrift.io;

/**
 * DeserializerFactory
 *
 * @param <E>
 */
public interface DeserializerFactory<E> {
    E createDeserializer();
}
