
package com.baidu.oped.apm.profiler.monitor.metric;

import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.profiler.monitor.metric.DynamicAcceptHistogram;
import com.baidu.oped.apm.profiler.monitor.metric.StaticAcceptHistogram;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * class WasStaticAcceptHistogramTest 
 *
 * @author meidongxu@baidu.com
 */
public class WasStaticAcceptHistogramTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testLookUp() throws Exception {
        StaticAcceptHistogram table = new StaticAcceptHistogram();
        Assert.assertTrue(table.addResponseTime("abc", ServiceType.TOMCAT.getCode(), 1000));
        Assert.assertTrue(table.addResponseTime("abc", ServiceType.BLOC.getCode(), 1000));
        Assert.assertTrue(table.addResponseTime("abc", ServiceType.STAND_ALONE.getCode(), 1000));
        Assert.assertTrue(table.addResponseTime("abc", ServiceType.BLOC.getCode(), 1000));

        Assert.assertFalse(table.addResponseTime("abc", ServiceType.ARCUS.getCode(), 1000));
    }


    public void performanceTest () throws InterruptedException {
//        63519 static table
//        72350 dynamic table
//        static version is faster.
        
        // Comparing InthashMap with ConcurrentHashMap
         // There's no big difference. double histogram? or single?
        // In addition, dynamic table creates ResponseKey objects.
        final StaticAcceptHistogram table = new StaticAcceptHistogram();
        execute(new Runnable() {
            @Override
            public void run() {
                table.addResponseTime("test", ServiceType.TOMCAT.getCode(), 1000);
            }
        });

        final DynamicAcceptHistogram hashTable = new DynamicAcceptHistogram();
        execute(new Runnable() {
            @Override
            public void run() {
                hashTable.addResponseTime("test", ServiceType.TOMCAT.getCode(), 1000);
            }
        });

    }

    private void execute(final Runnable runnable) throws InterruptedException {
        long l = System.currentTimeMillis();
        ExecutorService executors = Executors.newFixedThreadPool(20);
        for(int x = 0; x< 100; x++) {
            final int count = 1000000;
            final CountDownLatch latch = new CountDownLatch(count);
            for (int i = 0; i < count; i++) {
                executors.execute(new Runnable() {
                    @Override
                    public void run() {
                        runnable.run();
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }
        logger.debug("{}", System.currentTimeMillis() - l);
        executors.shutdown();
    }

}