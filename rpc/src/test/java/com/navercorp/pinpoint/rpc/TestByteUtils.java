
package com.baidu.oped.apm.rpc;

import java.util.Random;

/**
 * class TestByteUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class TestByteUtils {

    private static final Random RANDOM = new Random();

    private TestByteUtils() {
    }

    public static byte[] createRandomByte(int size) {
        byte[] bytes = new byte[size];
        RANDOM.nextBytes(bytes);
        return bytes;
    }
}
