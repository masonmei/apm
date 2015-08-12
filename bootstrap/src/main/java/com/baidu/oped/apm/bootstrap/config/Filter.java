package com.baidu.oped.apm.bootstrap.config;

/**
 * class Filter
 *
 * @author meidongxu@baidu.com
 */
public interface Filter<T> {
    public static final boolean FILTERED = true;

    boolean filter(T value);
}
