package com.baidu.oped.apm.statistics.collector.record.reader;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 8/31/15.
 */
public abstract class BaseReader<T extends AbstractPersistable<Long>> {

    private final long periodStart;
    private final long periodInMills;

    protected BaseReader(long periodStart, long periodInMills) {
        this.periodStart = periodStart;
        this.periodInMills = periodInMills;
    }

    /**
     * Get the startMills of the current period.
     *
     * @return
     */
    public long getPeriodStart() {
        return periodStart;
    }

    /**
     * Get the endMills of the current period.
     *
     * @return
     */
    public long getPeriodEnd() {
        return periodStart + periodInMills;
    }

    /**
     * Read the items of current period.
     *
     * @return
     */
    public abstract Iterable<T> readItems();
}
