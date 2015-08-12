
package com.baidu.oped.apm.collector.mapper.thrift;

import org.apache.thrift.TBase;

/**
 * class ThriftBoMapper 
 *
 * @author meidongxu@baidu.com
 */
public interface ThriftBoMapper<T, F extends TBase<?,?>> {

    T map(F thriftObject);
}
