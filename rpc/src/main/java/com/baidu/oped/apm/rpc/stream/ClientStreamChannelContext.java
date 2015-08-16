
package com.baidu.oped.apm.rpc.stream;

import com.baidu.oped.apm.rpc.util.AssertUtils;

/**
 * class ClientStreamChannelContext 
 *
 * @author meidongxu@baidu.com
 */
public class ClientStreamChannelContext extends StreamChannelContext {

    private final ClientStreamChannel clientStreamChannel;
    private final ClientStreamChannelMessageListener clientStreamChannelMessageListener;

    public ClientStreamChannelContext(ClientStreamChannel clientStreamChannel, ClientStreamChannelMessageListener clientStreamChannelMessageListener) {
        AssertUtils.assertNotNull(clientStreamChannel);
        AssertUtils.assertNotNull(clientStreamChannelMessageListener);

        this.clientStreamChannel = clientStreamChannel;
        this.clientStreamChannelMessageListener = clientStreamChannelMessageListener;
    }

    @Override
    public ClientStreamChannel getStreamChannel() {
        return clientStreamChannel;
    }

    public ClientStreamChannelMessageListener getClientStreamChannelMessageListener() {
        return clientStreamChannelMessageListener;
    }

}
