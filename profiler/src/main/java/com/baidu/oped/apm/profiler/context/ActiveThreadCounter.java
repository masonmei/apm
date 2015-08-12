
package com.baidu.oped.apm.profiler.context;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * class ActiveThreadCounter 
 *
 * @author meidongxu@baidu.com
 */
public class ActiveThreadCounter {
    private AtomicInteger counter = new AtomicInteger(0);

    public void start() {
        counter.incrementAndGet();
    }

    public void end() {
        counter.decrementAndGet();
    }

    public int getActiveThread() {
        return counter.get();
    }

    public void reset() {
        counter.set(0);
    }
}
