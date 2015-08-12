
package com.baidu.oped.apm.collector.handler;

import org.apache.thrift.TBase;
import org.springframework.stereotype.Service;

/**
 * class RequestResponseHandler 
 *
 * @author meidongxu@baidu.com
 */
//@Service
public interface RequestResponseHandler {

    TBase<?, ?> handleRequest(TBase<?, ?> tbase);
    
}
