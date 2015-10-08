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
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.ApplicationServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStat;
import com.baidu.oped.apm.common.jpa.entity.MetricData;
import com.baidu.oped.apm.common.jpa.repository.MetricDataRepository;

/**
 * Created by mason on 9/28/15.
 */
@Component
public class AppServerStatisticProcessor extends HostStatProcessor<ApplicationServerStatistic> {
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
                        return map.getAppId();
                    }
                }));
    }

    @Override
    protected ApplicationServerStatistic buildStatistic(Long appId, List<InstanceStat> list) {
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

        ApplicationServerStatistic applicationServerStatistic = new ApplicationServerStatistic();
        applicationServerStatistic.setAppId(appId);
        applicationServerStatistic.setGcOldCountMetric(gcOldCountMetric.getId());
        applicationServerStatistic.setGcOldTimeMetric(gcOldTimeMetric.getId());
        applicationServerStatistic.setHeapMaxMetric(heapMaxMetric.getId());
        applicationServerStatistic.setHeapUsedMetric(heapUsedMetric.getId());
        applicationServerStatistic.setNonHeapMaxMetric(nonHeapMaxMetric.getId());
        applicationServerStatistic.setNonHeapUsedMetric(nonHeapUsedMetric.getId());
        applicationServerStatistic.setJvmCpuMetric(vmCpuMetric.getId());
        applicationServerStatistic.setSystemCpuMetric(sysCpuMetric.getId());

        return applicationServerStatistic;
    }


}
