package com.baidu.oped.apm.statistics.collector.record.processor;

/**
 * Created by mason on 8/31/15.
 */
public abstract class BaseProcessor<F, T> {
    public abstract Iterable<T> process(Iterable<F> items);
}
