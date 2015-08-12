
package com.baidu.oped.apm.rpc.server.handler;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.rpc.server.ChannelContext;
import com.baidu.oped.apm.rpc.server.PinpointServerSocketStateCode;

/**
 * class ExecutionChannelStateChangeEventHandler 
 *
 * @author meidongxu@baidu.com
 */



public abstract class ExecutionChannelStateChangeEventHandler implements ChannelStateChangeEventHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final ChannelStateChangeEventHandler handler;
    private final Executor executor;
    
    public ExecutionChannelStateChangeEventHandler(ChannelStateChangeEventHandler handler, Executor executor) {
        this.handler = handler;
        this.executor = executor;
    }
    
    @Override
    public void eventPerformed(ChannelContext channelContext, PinpointServerSocketStateCode stateCode) {
        logger.info("{} eventPerformed {}:{}", this.getClass().getSimpleName(), channelContext, stateCode);

        Execution execution = new Execution(channelContext, stateCode);
        this.executor.execute(execution);
    }
    
    private class Execution implements Runnable {
        private final ChannelContext channelContext;
        private final PinpointServerSocketStateCode stateCode;

        public Execution(ChannelContext channelContext, PinpointServerSocketStateCode stateCode) {
            this.channelContext = channelContext;
            this.stateCode = stateCode;
        }
        
        @Override
        public void run() {
            try {
                handler.eventPerformed(channelContext, stateCode);
            } catch (Exception e) {
                handler.exceptionCaught(channelContext, stateCode, e);
            }
        }
    }

}
