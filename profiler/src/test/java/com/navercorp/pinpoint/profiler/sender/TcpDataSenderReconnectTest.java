
package com.baidu.oped.apm.profiler.sender;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.profiler.sender.TcpDataSender;
import com.baidu.oped.apm.rpc.PinpointSocketException;
import com.baidu.oped.apm.rpc.client.PinpointSocket;
import com.baidu.oped.apm.rpc.client.PinpointSocketFactory;
import com.baidu.oped.apm.rpc.packet.HandshakeResponseCode;
import com.baidu.oped.apm.rpc.packet.HandshakeResponseType;
import com.baidu.oped.apm.rpc.packet.RequestPacket;
import com.baidu.oped.apm.rpc.packet.SendPacket;
import com.baidu.oped.apm.rpc.server.PinpointServerSocket;
import com.baidu.oped.apm.rpc.server.ServerMessageListener;
import com.baidu.oped.apm.rpc.server.SocketChannel;
import com.baidu.oped.apm.thrift.dto.TApiMetaData;

/**
 * class TcpDataSenderReconnectTest 
 *
 * @author meidongxu@baidu.com
 */
public class TcpDataSenderReconnectTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final int PORT = 10050;
    public static final String HOST = "127.0.0.1";

    private int send;

    public PinpointServerSocket serverStart() {
        PinpointServerSocket server = new PinpointServerSocket();
        server.setMessageListener(new ServerMessageListener() {

            @Override
            public void handleSend(SendPacket sendPacket, SocketChannel channel) {
                logger.info("handleSend:{}", sendPacket);
                send++;
            }

            @Override
            public void handleRequest(RequestPacket requestPacket, SocketChannel channel) {
                logger.info("handleRequest:{}", requestPacket);
            }

            @Override
            public HandshakeResponseCode handleHandshake(Map properties) {
                return HandshakeResponseType.Success.DUPLEX_COMMUNICATION;
            }
        });
        server.bind(HOST, PORT);
        return server;
    }


    @Test
    public void connectAndSend() throws InterruptedException {
        PinpointServerSocket old = serverStart();

        PinpointSocketFactory socketFactory = createPinpointSocketFactory();
        PinpointSocket socket = createPinpointSocket(HOST, PORT, socketFactory);

        TcpDataSender sender = new TcpDataSender(socket);
        Thread.sleep(500);
        old.close();

        Thread.sleep(500);
        logger.info("Server start------------------");
        PinpointServerSocket pinpointServerSocket = serverStart();

        Thread.sleep(5000);
        logger.info("sendMessage------------------");
        sender.send(new TApiMetaData("test", System.currentTimeMillis(), 1, "TestApi"));

        Thread.sleep(500);
        logger.info("sender stop------------------");
        sender.stop();

        pinpointServerSocket.close();
        socket.close();
        socketFactory.release();
    }
    
    private PinpointSocketFactory createPinpointSocketFactory() {
        PinpointSocketFactory pinpointSocketFactory = new PinpointSocketFactory();
        pinpointSocketFactory.setTimeoutMillis(1000 * 5);
        pinpointSocketFactory.setProperties(Collections.EMPTY_MAP);

        return pinpointSocketFactory;
    }

    
    private PinpointSocket createPinpointSocket(String host, int port, PinpointSocketFactory factory) {
        PinpointSocket socket = null;
        for (int i = 0; i < 3; i++) {
            try {
                socket = factory.connect(host, port);
                logger.info("tcp connect success:{}/{}", host, port);
                return socket;
            } catch (PinpointSocketException e) {
                logger.warn("tcp connect fail:{}/{} try reconnect, retryCount:{}", host, port, i);
            }
        }
        logger.warn("change background tcp connect mode  {}/{} ", host, port);
        socket = factory.scheduledConnect(host, port);

        return socket;
    }
}
