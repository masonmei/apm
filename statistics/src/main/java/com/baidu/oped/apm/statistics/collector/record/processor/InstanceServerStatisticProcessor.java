package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.InstanceServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStat;
import com.baidu.oped.apm.common.jpa.entity.MetricData;
import com.baidu.oped.apm.common.jpa.repository.MetricDataRepository;

/**
 * Created by mason on 9/28/15.
 */
@Component
public class InstanceServerStatisticProcessor extends HostStatProcessor<InstanceServerStatistic> {
    @Autowired
    private MetricDataRepository metricDataRepository;

    public void setMetricDataRepository(MetricDataRepository metricDataRepository) {
        this.metricDataRepository = metricDataRepository;
    }

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
    protected InstanceServerStatistic buildStatistic(Long instanceId, List<InstanceStat> list) {
        DoubleSummaryStatistics vmCpuStatistics =
                list.stream().mapToDouble(InstanceStat::getJvmCpuLoad).summaryStatistics();
        DoubleSummaryStatistics sysCpuStatistics =
                list.stream().mapToDouble(InstanceStat::getSystemCpuLoad).summaryStatistics();
        DoubleSummaryStatistics heapUsedStatistics =
                list.stream().mapToDouble(InstanceStat::getJvmMemoryHeapUsed).summaryStatistics();
        DoubleSummaryStatistics heapMaxStatistics =
                list.stream().mapToDouble(InstanceStat::getJvmMemoryHeapMax).summaryStatistics();
        DoubleSummaryStatistics nonHeapMaxStatistics =
                list.stream().mapToDouble(InstanceStat::getJvmMemoryNonHeapMax).summaryStatistics();
        DoubleSummaryStatistics nonHeapUsedStatistics =
                list.stream().mapToDouble(InstanceStat::getJvmMemoryNonHeapUsed).summaryStatistics();
        DoubleSummaryStatistics gcOldCountStatistics =
                list.stream().mapToDouble(InstanceStat::getJvmGcOldCount).summaryStatistics();
        DoubleSummaryStatistics gcOldTimeStatistic =
                list.stream().mapToDouble(InstanceStat::getJvmGcOldTime).summaryStatistics();

        MetricData vmCpuMetric = buildMetricData(vmCpuStatistics);
        MetricData sysCpuMetric = buildMetricData(sysCpuStatistics);
        MetricData heapUsedMetric = buildMetricData(heapUsedStatistics);
        MetricData heapMaxMetric = buildMetricData(heapMaxStatistics);
        MetricData nonHeapUsedMetric = buildMetricData(nonHeapUsedStatistics);
        MetricData nonHeapMaxMetric = buildMetricData(nonHeapMaxStatistics);
        MetricData gcOldCountMetric = buildMetricData(gcOldCountStatistics);
        MetricData gcOldTimeMetric = buildMetricData(gcOldTimeStatistic);

        metricDataRepository
                .save(Arrays.asList(vmCpuMetric, sysCpuMetric, heapMaxMetric, heapUsedMetric, nonHeapMaxMetric,
                                    nonHeapUsedMetric, gcOldCountMetric, gcOldTimeMetric));

        InstanceServerStatistic statistic = new InstanceServerStatistic();
        statistic.setInstanceId(instanceId);
        statistic.setGcOldCountMetric(gcOldCountMetric.getId());
        statistic.setGcOldTimeMetric(gcOldTimeMetric.getId());
        statistic.setHeapMaxMetric(heapMaxMetric.getId());
        statistic.setHeapUsedMetric(heapUsedMetric.getId());
        statistic.setNonHeapMaxMetric(nonHeapMaxMetric.getId());
        statistic.setNonHeapUsedMetric(nonHeapUsedMetric.getId());
        statistic.setJvmCpuMetric(vmCpuMetric.getId());
        statistic.setSystemCpuMetric(sysCpuMetric.getId());

        return statistic;
    }
}
