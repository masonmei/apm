
package com.baidu.oped.apm.rpc.util;

import java.util.Map;

import com.baidu.oped.apm.rpc.control.ControlMessageDecoder;
import com.baidu.oped.apm.rpc.control.ControlMessageEncoder;
import com.baidu.oped.apm.rpc.control.ProtocolException;

/**
 * class ControlMessageEncodingUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class ControlMessageEncodingUtils {

    private static final ControlMessageEncoder encoder = new ControlMessageEncoder();
    private static final ControlMessageDecoder decoder = new ControlMessageDecoder();

    private ControlMessageEncodingUtils() {
    }

    public static byte[] encode(Map<String, Object> value) throws ProtocolException {
        return encoder.encode(value);
    }

    public static Object decode(byte[] in) throws ProtocolException {
        return decoder.decode(in);
    }

}
