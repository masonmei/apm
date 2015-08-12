
package com.baidu.oped.apm.common.util;

import java.util.concurrent.*;

/**
 * class ExecutorFactory 
 *
 * @author meidongxu@baidu.com
 */
public final class ExecutorFactory {

    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new PinpointThreadFactory("Pinpoint-defaultThreadFactory", true);

    private ExecutorFactory() {
    }

    public static ThreadPoolExecutor newFixedThreadPool(int nThreads, int workQueueMaxSize, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(workQueueMaxSize), threadFactory);
    }

    public static ThreadPoolExecutor newFixedThreadPool(int nThreads, int workQueueMaxSize) {
        return newFixedThreadPool(nThreads, workQueueMaxSize, DEFAULT_THREAD_FACTORY);
    }

    public static ThreadPoolExecutor newFixedThreadPool(int nThreads, int workQueueMaxSize, String threadFactoryName, boolean daemon) {
        ThreadFactory threadFactory = new PinpointThreadFactory(threadFactoryName, daemon);
        return newFixedThreadPool(nThreads, workQueueMaxSize, threadFactory);
    }

}
