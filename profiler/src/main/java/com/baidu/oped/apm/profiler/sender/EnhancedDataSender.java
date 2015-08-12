
package com.baidu.oped.apm.profiler.sender;

import com.baidu.oped.apm.rpc.FutureListener;
import com.baidu.oped.apm.rpc.ResponseMessage;
import com.baidu.oped.apm.rpc.client.PinpointSocketReconnectEventListener;

import org.apache.thrift.TBase;

/**
 * class EnhancedDataSender 
 *
 * @author meidongxu@baidu.com
 */
public interface EnhancedDataSender extends DataSender {

    boolean request(TBase<?, ?> data);
    boolean request(TBase<?, ?> data, int retry);
    boolean request(TBase<?, ?> data, FutureListener<ResponseMessage> listener);

    boolean addReconnectEventListener(PinpointSocketReconnectEventListener eventListener);
    boolean removeReconnectEventListener(PinpointSocketReconnectEventListener eventListener);

}
