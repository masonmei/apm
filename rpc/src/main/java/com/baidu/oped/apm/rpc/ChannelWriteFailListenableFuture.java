
package com.baidu.oped.apm.rpc;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * class ChannelWriteFailListenableFuture 
 *
 * @author meidongxu@baidu.com
 */
public class ChannelWriteFailListenableFuture<T> extends DefaultFuture<T> implements ChannelFutureListener {

    public ChannelWriteFailListenableFuture() {
        super(3000);
    }

    public ChannelWriteFailListenableFuture(long timeoutMillis) {
        super(timeoutMillis);
    }


    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (!future.isSuccess()) {
            // io write fail
            this.setFailure(future.getCause());
        }
    }
}
