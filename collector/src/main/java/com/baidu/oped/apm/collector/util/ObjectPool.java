package com.baidu.oped.apm.collector.util;

public interface ObjectPool<T> {
    PooledObject<T> getObject();

}
