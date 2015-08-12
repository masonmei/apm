package com.baidu.oped.apm.bootstrap.config;

/**
 * class SkipFilter
 *
 * @author meidongxu@baidu.com
 */
public class SkipFilter<T> implements Filter<T> {
    @Override
    public boolean filter(T value) {
        return false;
    }
}
