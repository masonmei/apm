package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.baidu.oped.apm.common.jpa.entity.AgentStatCpuLoad;
import com.baidu.oped.apm.common.jpa.entity.AgentStatMemoryGc;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;

/**
 * Created by mason on 8/31/15.
 */
public class InstanceMemoryProcessor extends MemoryProcessor<InstanceStatistic> {

    @Override
    public Iterable<InstanceStatistic> process(Iterable<AgentStatMemoryGc> items) {
        List<InstanceStatistic> statistics = new ArrayList<>();

        Map<Long, List<AgentStatMemoryGc>> grouped =
                StreamSupport.stream(items.spliterator(), false)
                        .collect(Collectors.groupingBy(AgentStatMemoryGc::getInstanceId));

        grouped.forEach((instanceId, list) -> {
            OptionalDouble average = list.stream()
                                             .mapToDouble(AgentStatMemoryGc::getJvmMemoryHeapUsed)
                                             .average();
            InstanceStatistic statistic = new InstanceStatistic();
            statistic.setInstanceId(instanceId);
            statistic.setCpuUsage(average.getAsDouble());
            statistics.add(statistic);
        });

        return statistics;
    }

}
