
package com.baidu.oped.apm.rpc.packet;

/**
 * class PacketType 
 *
 * @author meidongxu@baidu.com
 */
public class PacketType {
    public static final short APPLICATION_SEND = 1;
    public static final short APPLICATION_TRACE_SEND = 2;
    public static final short APPLICATION_TRACE_SEND_ACK = 3;

    public static final short APPLICATION_REQUEST = 5;
    public static final short APPLICATION_RESPONSE = 6;


    public static final short APPLICATION_STREAM_CREATE = 10;
    public static final short APPLICATION_STREAM_CREATE_SUCCESS = 12;
    public static final short APPLICATION_STREAM_CREATE_FAIL = 14;

    public static final short APPLICATION_STREAM_CLOSE = 15;

    public static final short APPLICATION_STREAM_PING = 17;
    public static final short APPLICATION_STREAM_PONG = 18;
    
    public static final short APPLICATION_STREAM_RESPONSE = 20;

    
    public static final short CONTROL_CLIENT_CLOSE = 100;
    public static final short CONTROL_SERVER_CLOSE = 110;

    // control packet
    public static final short CONTROL_HANDSHAKE = 150;
    public static final short CONTROL_HANDSHAKE_RESPONSE = 151;

    // keep stay because of performance in case of ping and pong. others removed.
    public static final short CONTROL_PING = 200;
    public static final short CONTROL_PONG = 201;

    public static final short UNKNOWN = 500;

    public static final int PACKET_TYPE_SIZE = 2;
}
