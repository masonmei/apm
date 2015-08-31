package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.baidu.oped.apm.common.jpa.entity.AgentStatCpuLoad;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;

/**
 * Created by mason on 8/31/15.
 */
public class InstanceCpuProcessor extends CpuProcessor<InstanceStatistic> {

    @Override
    public Iterable<InstanceStatistic> process(Iterable<AgentStatCpuLoad> items) {
        List<InstanceStatistic> statistics = new ArrayList<>();

        Map<Long, List<AgentStatCpuLoad>> grouped =
                StreamSupport.stream(items.spliterator(), false)
                        .collect(Collectors.groupingBy(AgentStatCpuLoad::getInstanceId));

        grouped.forEach((instanceId, list) -> {
            OptionalDouble average = list.stream()
                                             .mapToDouble(AgentStatCpuLoad::getJvmCpuLoad)
                                             .average();
            InstanceStatistic statistic = new InstanceStatistic();
            statistic.setInstanceId(instanceId);
            statistic.setCpuUsage(average.getAsDouble());
            statistics.add(statistic);
        });

        return statistics;
    }

}
