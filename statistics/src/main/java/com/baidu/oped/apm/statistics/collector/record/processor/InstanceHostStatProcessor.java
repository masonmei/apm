package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.InstanceStat;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class InstanceHostStatProcessor extends HostStatProcessor<InstanceStatistic> {

    @Override
    protected Map<Long, List<InstanceStat>> group(Iterable<InstanceStat> items) {
        final Map<Long, AgentInstanceMap> maps = getAgentInstanceMaps(items);

        return StreamSupport.stream(items.spliterator(), false)
                .collect(Collectors.groupingBy(new Function<InstanceStat, Long>() {
                    @Override
                    public Long apply(InstanceStat t) {
                        AgentInstanceMap map = maps.get(t.getAgentId());
                        return map.getInstanceId();
                    }
                }));
    }

    @Override
    protected InstanceStatistic buildStatistic(Long instanceId, List<InstanceStat> list) {
        OptionalDouble cpuAverage = list.stream().mapToDouble(InstanceStat::getJvmCpuLoad).average();
        OptionalDouble memAverage = list.stream().mapToDouble(InstanceStat::getJvmMemoryHeapUsed).average();
        InstanceStatistic statistic = new InstanceStatistic();
        statistic.setInstanceId(instanceId);
        statistic.setCpuUsage(cpuAverage.getAsDouble());
        statistic.setMemoryUsage(memAverage.getAsDouble());
        return statistic;
    }

}
