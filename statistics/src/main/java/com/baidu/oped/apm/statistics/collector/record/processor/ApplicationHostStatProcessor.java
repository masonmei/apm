package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStat;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class ApplicationHostStatProcessor extends HostStatProcessor<ApplicationStatistic> {

    @Override
    protected Map<Long, List<InstanceStat>> group(Iterable<InstanceStat> items) {
        final Map<Long, AgentInstanceMap> maps = getAgentInstanceMaps(items);
        return StreamSupport.stream(items.spliterator(), false)
                .collect(Collectors.groupingBy(new Function<InstanceStat, Long>() {
                    @Override
                    public Long apply(InstanceStat t) {
                        AgentInstanceMap map = maps.get(t.getAgentId());
                        return map.getAppId();
                    }
                }));

    }

    @Override
    protected ApplicationStatistic buildStatistic(Long groupId, List<InstanceStat> list) {
        OptionalDouble cpuAverage = list.stream().mapToDouble(InstanceStat::getJvmCpuLoad).average();
        OptionalDouble memAverage = list.stream().mapToDouble(InstanceStat::getJvmMemoryHeapUsed).average();
        ApplicationStatistic statistic = new ApplicationStatistic();
        statistic.setAppId(groupId);
        statistic.setCpuUsage(cpuAverage.getAsDouble());
        statistic.setMemoryUsage(memAverage.getAsDouble());
        return statistic;
    }

}
