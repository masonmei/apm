
package com.baidu.oped.apm.collector.util;

import org.apache.hadoop.hbase.util.Bytes;

import java.net.DatagramPacket;

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
        return Bytes.toStringBinary(datagramPacket.getData(), 0, datagramPacket.getLength());
    }

    public static String dumpByteArray(byte[] bytes) {
        if (bytes == null) {
            return "null";
        }
        return Bytes.toStringBinary(bytes, 0, bytes.length);
    }
}
