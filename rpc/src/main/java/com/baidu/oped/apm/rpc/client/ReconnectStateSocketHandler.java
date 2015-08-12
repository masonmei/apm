
package com.baidu.oped.apm.rpc.client;

import com.baidu.oped.apm.rpc.DefaultFuture;
import com.baidu.oped.apm.rpc.Future;
import com.baidu.oped.apm.rpc.PinpointSocketException;
import com.baidu.oped.apm.rpc.ResponseMessage;
import com.baidu.oped.apm.rpc.stream.ClientStreamChannelContext;
import com.baidu.oped.apm.rpc.stream.ClientStreamChannelMessageListener;
import com.baidu.oped.apm.rpc.stream.StreamChannelContext;

import java.net.SocketAddress;

/**
 * class ReconnectStateSocketHandler 
 *
 * @author meidongxu@baidu.com
 */
public class ReconnectStateSocketHandler implements SocketHandler {


    @Override
    public void setConnectSocketAddress(SocketAddress connectSocketAddress) {
    }

    @Override
    public void open() {
        throw new IllegalStateException();
    }

    @Override
    public void initReconnect() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setPinpointSocket(PinpointSocket pinpointSocket) {
    }

    @Override
    public void sendSync(byte[] bytes) {
        throw newReconnectException();
    }

    @Override
    public Future sendAsync(byte[] bytes) {
        return reconnectFailureFuture();
    }

    private DefaultFuture<ResponseMessage> reconnectFailureFuture() {
        DefaultFuture<ResponseMessage> reconnect = new DefaultFuture<ResponseMessage>();
        reconnect.setFailure(newReconnectException());
        return reconnect;
    }

    @Override
    public void close() {
    }

    @Override
    public void send(byte[] bytes) {
    }

    private PinpointSocketException newReconnectException() {
        return new PinpointSocketException("reconnecting...");
    }

    @Override
    public Future<ResponseMessage> request(byte[] bytes) {
        return reconnectFailureFuture();
    }

    @Override
    public ClientStreamChannelContext createStreamChannel(byte[] payload, ClientStreamChannelMessageListener clientStreamChannelMessageListener) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public StreamChannelContext findStreamChannel(int streamChannelId) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void sendPing() {
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isSupportServerMode() {
        return false;
    }

    @Override
    public void doHandshake() {
//        throw new UnsupportedOperationException();
    }

}
