
package com.baidu.oped.apm.collector.receiver;

import org.apache.thrift.TBase;

/**
 * class DispatchHandler 
 *
 * @author meidongxu@baidu.com
 */
public interface DispatchHandler {

    // Separating Send and Request. That dose not be satisfied but try to change that later.

    void dispatchSendMessage(TBase<?, ?> tBase);

    TBase dispatchRequestMessage(TBase<?, ?> tBase);

}
