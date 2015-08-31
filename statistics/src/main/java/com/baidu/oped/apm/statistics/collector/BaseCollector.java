package com.baidu.oped.apm.statistics.collector;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.Assert;

/**
 * Created by mason on 8/29/15.
 */
public abstract class BaseCollector<F extends AbstractPersistable<Long>, T extends AbstractPersistable<Long>> {
    private long periodStart;
    private long periodInMills;

    /**
     * Select original record from database.
     *
     * @return records from database.
     */
    public abstract Iterable<F> getPeriodData();

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
     * split the original data by id to map.
     *
     * @param data the original data from database.
     *
     * @return split map
     */
    public Map<Long, List<F>> groupById(Iterable<F> data) {
        Assert.notNull(data, "Cannot group by id for null object.");
        return StreamSupport.stream(data.spliterator(), false)
                       .collect(Collectors.groupingBy(F::getId));
    }

    public void collect() {
        Iterable<F> periodData = getPeriodData();
        Map<Long, List<F>> splitMap = groupById(periodData);
        splitMap.forEach((id, list) -> {
            T result = calculator(id, list);
            saveOrUpdate(result);
        });
    }

    protected abstract void saveOrUpdate(T result);

    public abstract T calculator(Long id, List<F> list);
}
