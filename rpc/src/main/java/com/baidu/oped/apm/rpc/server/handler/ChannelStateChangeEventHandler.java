
package com.baidu.oped.apm.rpc.server.handler;

import com.baidu.oped.apm.rpc.server.ChannelContext;
import com.baidu.oped.apm.rpc.server.PinpointServerSocketStateCode;

/**
 * class ChannelStateChangeEventHandler 
 *
 * @author meidongxu@baidu.com
 */
public interface ChannelStateChangeEventHandler {

    void eventPerformed(ChannelContext channelContext, PinpointServerSocketStateCode stateCode) throws Exception;
    
    void exceptionCaught(ChannelContext channelContext, PinpointServerSocketStateCode stateCode, Throwable e);

}
