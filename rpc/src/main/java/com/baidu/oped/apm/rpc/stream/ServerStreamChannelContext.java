
package com.baidu.oped.apm.rpc.stream;

/**
 * class ServerStreamChannelContext 
 *
 * @author meidongxu@baidu.com
 */



public class ServerStreamChannelContext extends StreamChannelContext {

    private ServerStreamChannel streamChannel;

    public ServerStreamChannelContext(ServerStreamChannel streamChannel) {
        this.streamChannel = streamChannel;
    }

    @Override
    public ServerStreamChannel getStreamChannel() {
        return streamChannel;
    }

}
