package com.baidu.oped.apm.collector.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mason on 11/12/15.
 */
public class DefaultObjectPool<T> implements ObjectPool<T> {
    // you don't need a blocking queue. There must be enough objects in a queue. if not, it means leakage.
    private final Queue<PooledObject<T>> queue = new ConcurrentLinkedQueue<PooledObject<T>>();

    private final ObjectPoolFactory<T> factory;

    public DefaultObjectPool(ObjectPoolFactory<T> factory, int size) {
        if (factory == null) {
            throw new NullPointerException("factory");
        }
        this.factory = factory;
        fill(size);
    }

    private void fill(int size) {
        for (int i = 0; i < size; i++) {
            PooledObjectWrapper wrapper = createObject();
            queue.offer(wrapper);
        }
    }

    private PooledObjectWrapper createObject() {
        T t = this.factory.create();
        return new PooledObjectWrapper(t);
    }

    @Override
    public PooledObject<T> getObject() {
        PooledObject<T> object = queue.poll();
        if (object == null) {
            // create dynamically ???
            return createObject();
        }
        return object;
    }


    private void returnObject(PooledObject<T> t) {
        if (t == null) {
            return;
        }
        factory.beforeReturn(t.getObject());
        queue.offer(t);
    }

    public int size() {
        return queue.size();
    }

    private class PooledObjectWrapper implements PooledObject<T> {
        private final T value;

        public PooledObjectWrapper(T value) {
            if (value == null) {
                throw new NullPointerException("value must not be null");
            }
            this.value = value;
        }

        @Override
        public T getObject() {
            return value;
        }

        @Override
        public void returnObject() {
            DefaultObjectPool.this.returnObject(this);
        }
    }

}
