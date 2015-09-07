package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.oped.apm.common.jpa.entity.InstanceStat;

/**
 * Created by mason on 8/31/15.
 */
public abstract class HostStatProcessor<T> extends BaseProcessor<InstanceStat, T> {

    public Iterable<T> process(Iterable<InstanceStat> items) {
        List<T> statistics = new ArrayList<>();
        Map<Long, List<InstanceStat>> grouped = group(items);
        grouped.forEach((instanceId, list) -> statistics.add(buildStatistic(instanceId, list)));
        return statistics;
    }

    protected abstract Map<Long, List<InstanceStat>> group(Iterable<InstanceStat> items);

    protected abstract T buildStatistic(Long instanceId, List<InstanceStat> list);
}
