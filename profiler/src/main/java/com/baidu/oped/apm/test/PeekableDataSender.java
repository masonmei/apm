
package com.baidu.oped.apm.test;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.thrift.TBase;

import com.baidu.oped.apm.profiler.sender.DataSender;

/**
 * class PeekableDataSender 
 *
 * @author meidongxu@baidu.com
 */
public class PeekableDataSender<T extends TBase<?, ?>> implements DataSender, Iterable<T> {

    private final Queue<T> queue = new ConcurrentLinkedQueue<T>();

    public T peek() {
        return this.queue.peek();
    }

    public T poll() {
        return this.queue.poll();
    }

    public int size() {
        return this.queue.size();
    }

    public void clear() {
        this.queue.clear();
    }

    @Override
    public Iterator<T> iterator() {
        return this.queue.iterator();
    }

    @Override
    public boolean send(TBase<?, ?> data) {
        // don't do deep copy. 
        // because other datasenders preserve references of objects to send if network transmission is delayed
        @SuppressWarnings("unchecked")
        T dataToAdd = (T)data;
        return this.queue.offer(dataToAdd);
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isNetworkAvailable() {
        return false;
    }

    @Override
    public String toString() {
        return this.queue.toString();
    }

}
