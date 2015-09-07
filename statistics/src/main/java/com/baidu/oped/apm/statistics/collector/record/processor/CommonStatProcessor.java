package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.oped.apm.common.jpa.entity.Trace;

/**
 * Created by mason on 8/31/15.
 */
public abstract class CommonStatProcessor<T> extends BaseProcessor<Trace, T> {

    public Iterable<T> process(Iterable<Trace> items) {
        List<T> statistics = new ArrayList<>();
        Map<Long, List<Trace>> grouped = group(items);
        grouped.forEach((instanceId, list) -> statistics.add(buildStatistic(instanceId, list)));
        return statistics;
    }

    protected abstract Map<Long, List<Trace>> group(Iterable<Trace> items);

    protected abstract T buildStatistic(Long instanceId, List<Trace> list);
}
