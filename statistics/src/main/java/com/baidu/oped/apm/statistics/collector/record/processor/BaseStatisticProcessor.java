package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.ArrayList;
import java.util.List;

import com.baidu.oped.apm.common.jpa.entity.BaseStatistic;
import com.baidu.oped.apm.common.jpa.entity.Trace;

/**
 * Created by mason on 8/31/15.
 */
public abstract class BaseStatisticProcessor<T extends BaseStatistic> extends BaseProcessor<Trace, T> {

    @Override
    public Iterable<T> process(Iterable<Trace> items) {
        List<T> statistics = new ArrayList<>();

        //        Map<Long, List<Trace>> grouped =
        //                StreamSupport.stream(items.spliterator(), false)
        //                        .collect(Collectors.groupingBy(getClass()::groupBy));
        //
        //        grouped.forEach((instanceId, list) -> {
        //            OptionalDouble average = list.stream()
        //                                             .mapToDouble(AgentStatMemoryGc::getJvmMemoryHeapUsed)
        //                                             .summaryStatistics();
        //            T statistic = newStatisticInstance();
        //
        //
        //
        //            statistics.add(statistic);
        //        });

        return statistics;
    }

    public abstract T newStatisticInstance();

    public abstract T groupBy();

}
