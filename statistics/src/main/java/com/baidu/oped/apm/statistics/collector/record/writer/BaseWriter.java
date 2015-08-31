package com.baidu.oped.apm.statistics.collector.record.writer;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.BaseStatistic;
import com.baidu.oped.apm.common.jpa.entity.HostStatistic;

/**
 * Created by mason on 8/31/15.
 */
public abstract class BaseWriter<T extends AbstractPersistable<Long>> {

    private final long periodStart;
    private final long periodInMills;

    protected BaseWriter(long periodStart, long periodInMills) {
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
     * Get the period value.
     *
     * @return
     */
    public long getPeriod() {
        return periodInMills;
    }

    /**
     * Write statistic items to database.
     *
     * @param items
     */
    public void writeItems(Iterable<T> items) {
        items.forEach(this::writeItem);
    }

    /**
     * Create or update a statistic item.
     *
     * @param item
     */
    protected abstract void writeItem(T item);

    /**
     * Copy non-null properties from object to object.
     *
     * @param from
     * @param to
     */
    protected void copyStatisticValue(T from, T to) {
        Assert.notNull(from, "Cannot copy statistic from null object.");
        Assert.notNull(to, "Cannot copy statistic to null object.");

        if (from instanceof BaseStatistic) {
            BaseStatistic baseFrom = (BaseStatistic) from;
            BaseStatistic baseTo = (BaseStatistic) to;

            copyBaseStatisticNotNullProperty(baseFrom, baseTo);
        }

        if (from instanceof HostStatistic) {
            HostStatistic hostFrom = (HostStatistic) from;
            HostStatistic hostTo = (HostStatistic) to;

            copyHostStatisticNotNullProperty(hostFrom, hostTo);
        }
    }

    private void copyHostStatisticNotNullProperty(HostStatistic hostFrom, HostStatistic hostTo) {
        Double cpuUsage = hostFrom.getCpuUsage();
        if (cpuUsage != null) {
            hostTo.setCpuUsage(cpuUsage);
        }

        Double memoryUsage = hostFrom.getMemoryUsage();
        if (memoryUsage != null) {
            hostTo.setMemoryUsage(memoryUsage);
        }
    }

    private void copyBaseStatisticNotNullProperty(BaseStatistic baseFrom, BaseStatistic baseTo) {
        Double sumResponseTime = baseFrom.getSumResponseTime();
        if (sumResponseTime != null) {
            baseTo.setSumResponseTime(sumResponseTime);
        }

        Double maxResponseTime = baseFrom.getMaxResponseTime();
        if (maxResponseTime != null) {
            baseTo.setMaxResponseTime(maxResponseTime);
        }

        Double minResponseTime = baseFrom.getMinResponseTime();
        if (minResponseTime != null) {
            baseTo.setMinResponseTime(minResponseTime);
        }

        Long pv = baseFrom.getPv();
        if (pv != null) {
            baseTo.setPv(pv);
        }

        Long error = baseFrom.getError();
        if (error != null) {
            baseTo.setError(error);
        }

        Long satisfied = baseFrom.getSatisfied();
        if (satisfied != null) {
            baseTo.setSatisfied(satisfied);
        }

        Long tolerated = baseFrom.getTolerated();
        if (tolerated != null) {
            baseTo.setTolerated(tolerated);
        }

        Long frustrated = baseFrom.getFrustrated();
        if (frustrated != null) {
            baseTo.setFrustrated(frustrated);
        }
    }
}
