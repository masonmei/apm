
package com.baidu.oped.apm.bootstrap.interceptor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * class AtomicMaxUpdater 
 *
 * @author meidongxu@baidu.com
 */
public class AtomicMaxUpdater {

    private final AtomicInteger maxIndex = new AtomicInteger(-1);

    public boolean updateMax(int max) {
        while (true) {
            final int currentMax = maxIndex.get();
            if (currentMax >= max) {
                return false;
            }
            final boolean update = maxIndex.compareAndSet(currentMax, max);
            if (update) {
                return true;
            }
        }
    }

    public int getIndex() {
        return maxIndex.get();
    }
}
