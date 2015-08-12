
package com.baidu.oped.apm.rpc.server;

import com.baidu.oped.apm.rpc.DiscardPipelineFactory;
import com.baidu.oped.apm.rpc.server.PinpointServerSocket;
import com.baidu.oped.apm.rpc.util.PinpointRPCTestUtils;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

/**
 * class PinpointServerSocketTest 
 *
 * @author meidongxu@baidu.com
 */
public class PinpointServerSocketTest {
    
    private static int bindPort;
    
    @BeforeClass
    public static void setUp() throws IOException {
        bindPort = PinpointRPCTestUtils.findAvailablePort();
    }
    
    @Test
    public void testBind() throws Exception {
        PinpointServerSocket serverSocket = new PinpointServerSocket();
        serverSocket.setPipelineFactory(new DiscardPipelineFactory());
        serverSocket.bind("127.0.0.1", bindPort);

        Socket socket = new Socket("127.0.0.1", bindPort);
        socket.getOutputStream().write(new byte[10]);
        socket.getOutputStream().flush();
        socket.close();

        Thread.sleep(1000);
        PinpointRPCTestUtils.close(serverSocket);
    }


}
