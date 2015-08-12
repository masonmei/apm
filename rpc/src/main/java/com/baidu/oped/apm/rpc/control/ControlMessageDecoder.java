
package com.baidu.oped.apm.rpc.control;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * class ControlMessageDecoder 
 *
 * @author meidongxu@baidu.com
 */
public class ControlMessageDecoder {

    private Charset charset;

    public ControlMessageDecoder() {
        this.charset = Charset.forName("UTF-8");
    }

    public Object decode(byte[] in) throws ProtocolException {
        return decode(ByteBuffer.wrap(in));
    }

    public Object decode(ByteBuffer in) throws ProtocolException {
        byte type = in.get();
        switch (type) {
        case ControlMessageProtocolConstant.TYPE_CHARACTER_NULL:
            return null;
        case ControlMessageProtocolConstant.TYPE_CHARACTER_BOOL_TRUE:
            return Boolean.TRUE;
        case ControlMessageProtocolConstant.TYPE_CHARACTER_BOOL_FALSE:
            return Boolean.FALSE;
        case ControlMessageProtocolConstant.TYPE_CHARACTER_INT:
            return in.getInt();
        case ControlMessageProtocolConstant.TYPE_CHARACTER_LONG:
            return in.getLong();
        case ControlMessageProtocolConstant.TYPE_CHARACTER_DOUBLE:
            return Double.longBitsToDouble(in.getLong());
        case ControlMessageProtocolConstant.TYPE_CHARACTER_STRING:
            return decodeString(in);
        case ControlMessageProtocolConstant.CONTROL_CHARACTER_LIST_START:
            List<Object> answerList = new ArrayList<Object>();
            while (!isListFinished(in)) {
                answerList.add(decode(in));
            }
            in.get(); // Skip the terminator
            return answerList;
        case ControlMessageProtocolConstant.CONTROL_CHARACTER_MAP_START:
            Map<Object, Object> answerMap = new LinkedHashMap<Object, Object>();
            while (!isMapFinished(in)) {
                Object key = decode(in);
                Object value = decode(in);
                answerMap.put(key, value);
            }
            in.get(); // Skip the terminator
            return answerMap;
        default:
            throw new ProtocolException("invalid type character: " + (char) type + " (" + "0x" + Integer.toHexString(type) + ")");
        }
    }

    private Object decodeString(ByteBuffer in) {
        int length = readStringLength(in);

        byte[] bytesToEncode = new byte[length];
        in.get(bytesToEncode);

        return new String(bytesToEncode, charset);
    }

    private boolean isMapFinished(ByteBuffer in) {
        return in.get(in.position()) == ControlMessageProtocolConstant.CONTROL_CHARACTER_MAP_END;
    }

    private boolean isListFinished(ByteBuffer in) {
        return in.get(in.position()) == ControlMessageProtocolConstant.CONTROL_CHARACTER_LIST_END;
    }

    private int readStringLength(ByteBuffer in) {
        int result = 0;
        int shift = 0;

        while (true) {
            byte b = in.get();
            result |= (b & 0x7F) << shift;
            if ((b & 0x80) != 128)
                break;
            shift += 7;
        }
        return result;
    }

}
