
package com.baidu.oped.apm.profiler.util;

import java.util.Random;

/**
 * class ErrorInjectUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class ErrorInjectUtils {

    private static final Random random = new Random();

    private ErrorInjectUtils() {
    }

    public static void randomSleep(int mod) {
        int i = Math.abs(random.nextInt() % mod);
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
