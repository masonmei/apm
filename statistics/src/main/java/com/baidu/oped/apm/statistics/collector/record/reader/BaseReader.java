package com.baidu.oped.apm.statistics.collector.record.reader;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 8/31/15.
 */
public abstract class BaseReader<T extends AbstractPersistable<Long>> {

    /**
     * Read items of the given period from database.
     *
     * @param periodStart
     * @param periodInMills
     * @return
     */
    public abstract Iterable<T> readItems(long periodStart, long periodInMills);
}
