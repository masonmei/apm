package com.baidu.oped.apm.statistics.collector;

/**
 * Created by mason on 8/29/15.
 */
public interface BaseCollector {

    void collect(final long periodStart, final long periodInMills);
}
