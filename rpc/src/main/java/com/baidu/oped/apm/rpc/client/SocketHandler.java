
package com.baidu.oped.apm.rpc.client;

import java.net.SocketAddress;

import com.baidu.oped.apm.rpc.Future;
import com.baidu.oped.apm.rpc.ResponseMessage;
import com.baidu.oped.apm.rpc.stream.ClientStreamChannelContext;
import com.baidu.oped.apm.rpc.stream.ClientStreamChannelMessageListener;
import com.baidu.oped.apm.rpc.stream.StreamChannelContext;

/**
 * class SocketHandler 
 *
 * @author meidongxu@baidu.com
 */
public interface SocketHandler {

    void setConnectSocketAddress(SocketAddress address);

    void open();

    void initReconnect();

    void setPinpointSocket(PinpointSocket pinpointSocket);

    void sendSync(byte[] bytes);

    Future sendAsync(byte[] bytes);

    void close();

    void send(byte[] bytes);

    Future<ResponseMessage> request(byte[] bytes);

    ClientStreamChannelContext createStreamChannel(byte[] payload, ClientStreamChannelMessageListener clientStreamChannelMessageListener);

    StreamChannelContext findStreamChannel(int streamChannelId);
    
    void sendPing();

    boolean isConnected();

    boolean isSupportServerMode();

    void doHandshake();

}
