
package com.baidu.oped.apm.collector.util;

import java.net.DatagramPacket;

import com.baidu.oped.apm.common.util.BytesUtils;

/**
 * class PacketUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class PacketUtils {
    private PacketUtils() {
    }

    public static String dumpDatagramPacket(DatagramPacket datagramPacket) {
        if (datagramPacket == null) {
            return "null";
        }
        return BytesUtils.toString(datagramPacket.getData(), 0, datagramPacket.getLength());
    }

    public static String dumpByteArray(byte[] bytes) {
        if (bytes == null) {
            return "null";
        }
        return BytesUtils.toString(bytes, 0, bytes.length);
    }
}
