
package com.baidu.oped.apm.rpc.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.rpc.server.ChannelContext;
import com.baidu.oped.apm.rpc.server.PinpointServerSocketStateCode;

/**
 * class DoNothingChannelStateEventHandler 
 *
 * @author meidongxu@baidu.com
 */
public class DoNothingChannelStateEventHandler implements ChannelStateChangeEventHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final ChannelStateChangeEventHandler INSTANCE = new DoNothingChannelStateEventHandler();

    @Override
    public void eventPerformed(ChannelContext channelContext, PinpointServerSocketStateCode stateCode) {
        logger.info("{} eventPerformed {}:{}", this.getClass().getSimpleName(), channelContext, stateCode);
    }
    
    @Override
    public void exceptionCaught(ChannelContext channelContext, PinpointServerSocketStateCode stateCode, Throwable e) {
        if (logger.isWarnEnabled()) {
            logger.warn(this.getClass().getSimpleName() + " exception occured. Error: " + e.getMessage() + "."  , e);
        }
    }

}
