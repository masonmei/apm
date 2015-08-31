package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.baidu.oped.apm.common.jpa.entity.AgentStatCpuLoad;
import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;

/**
 * Created by mason on 8/31/15.
 */
public class ApplicationCpuProcessor extends CpuProcessor<ApplicationStatistic> {

    @Override
    public Iterable<ApplicationStatistic> process(Iterable<AgentStatCpuLoad> items) {
        List<ApplicationStatistic> statistics = new ArrayList<>();

        Map<Long, List<AgentStatCpuLoad>> grouped = StreamSupport.stream(items.spliterator(), false)
                                                            .collect(Collectors.groupingBy(AgentStatCpuLoad::getAppId));

        grouped.forEach((appId, list) -> {
            OptionalDouble average = list.stream()
                                             .mapToDouble(AgentStatCpuLoad::getJvmCpuLoad)
                                             .average();
            ApplicationStatistic statistic = new ApplicationStatistic();
            statistic.setAppId(appId);
            statistic.setCpuUsage(average.getAsDouble());
            statistics.add(statistic);
        });

        return statistics;
    }

}
