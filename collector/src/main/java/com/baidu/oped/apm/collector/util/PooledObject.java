package com.baidu.oped.apm.collector.util;

public interface PooledObject<T> {
    T getObject();

    void returnObject();
}
