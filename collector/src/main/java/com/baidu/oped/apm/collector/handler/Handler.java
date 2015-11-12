
package com.baidu.oped.apm.collector.handler;

import org.apache.thrift.TBase;

/**
 * class Handler 
 *
 * @author meidongxu@baidu.com
 */
public interface Handler {

    void handle(TBase<?, ?> tbase);
    
}
