
package com.baidu.oped.apm.rpc;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.oped.apm.rpc.DefaultFuture;
import com.baidu.oped.apm.rpc.Future;
import com.baidu.oped.apm.rpc.FutureListener;

/**
 * class FutureTest 
 *
 * @author meidongxu@baidu.com
 */
public class FutureTest {

    @Test
    public void simpleTest1() {
        DefaultFuture<String> future = new DefaultFuture<String>();

        SimpleListener<String> listener1 = new SimpleListener<String>();

        future.setListener(listener1);
//        future.addListener(listener2);

        Assert.assertFalse(listener1.isFinished());
//        Assert.assertFalse(listener2.isFinished());

        future.setResult("Hello");

        Assert.assertTrue(listener1.isFinished());
//        Assert.assertTrue(listener2.isFinished());
    }

    @Test
    public void simpleTest2() {
        DefaultFuture<String> future = new DefaultFuture<String>();

        SimpleListener<String> listener = new SimpleListener<String>();

        future.setResult("Hello");

        future.setListener(listener);

        Assert.assertTrue(listener.isFinished());
    }

    static class SimpleListener<T> implements FutureListener<T> {

        private final AtomicBoolean isFinished = new AtomicBoolean(false);

        @Override
        public void onComplete(Future<T> future) {
            isFinished.compareAndSet(false, true);
        }

        public boolean isFinished() {
            return isFinished.get();
        }
    }

}
