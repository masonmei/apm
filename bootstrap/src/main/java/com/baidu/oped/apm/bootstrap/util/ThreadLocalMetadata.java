package com.baidu.oped.apm.bootstrap.util;

/**
 * class ThreadLocalMetadata
 *
 * @author meidongxu@baidu.com
 */

/**
 * WARNING
 * This class stores data inside ThreadLocal. 
 * Data may be passed across proxy class boundaries, but they should be stateless.
 * This class should only be used to temporarily store stateless data such as method arguments and share it down the
 * chain.
 * This class should NEVER store stateful data.
 *
 * @author emeroad
 */
@Deprecated
public class ThreadLocalMetadata<T> {
    private final String name;
    private final ThreadLocal<T> threadLocal;

    public ThreadLocalMetadata(String metadataName) {
        this.name = metadataName;
        this.threadLocal = new ThreadLocal<T>();
    }

    public void set(T object) {
        threadLocal.set(object);
    }

    public T get() {
        return threadLocal.get();
    }

    public void remove() {
        threadLocal.remove();
    }

    public T getAndRemove() {
        final ThreadLocal<T> threadLocal = this.threadLocal;
        final T t = threadLocal.get();
        threadLocal.remove();
        return t;
    }
}
