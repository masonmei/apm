
package com.baidu.oped.apm.collector.handler;

import org.apache.thrift.TBase;

/**
 * class SimpleHandler 
 *
 * @author meidongxu@baidu.com
 */
public interface SimpleHandler {

    void handleSimple(TBase<?, ?> tbase);
    
}
