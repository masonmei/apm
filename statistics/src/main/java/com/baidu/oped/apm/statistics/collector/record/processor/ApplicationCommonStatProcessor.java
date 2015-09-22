package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.Trace;
import com.baidu.oped.apm.statistics.collector.ApdexDecider;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class ApplicationCommonStatProcessor extends CommonStatProcessor<ApplicationStatistic> {

    @Autowired
    private ApdexDecider decider;

    public void setDecider(ApdexDecider decider) {
        this.decider = decider;
    }

    @Override
    protected Map<Long, List<Trace>> group(Iterable<Trace> items) {
        final Map<Long, AgentInstanceMap> maps = getAgentInstanceMaps(items);
        return StreamSupport.stream(items.spliterator(), false)
                .collect(Collectors.groupingBy(new Function<Trace, Long>() {
                    @Override
                    public Long apply(Trace t) {
                        AgentInstanceMap map = maps.get(t.getAgentId());
                        return map.getAppId();
                    }
                }));
    }

    @Override
    protected ApplicationStatistic buildStatistic(Long groupId, List<Trace> list) {
        DoubleSummaryStatistics summaryStatistics = list.stream().mapToDouble(Trace::getElapsed).summaryStatistics();
        Long errorCount = list.stream().filter(trace -> trace.getErrCode() == 1).count();
        Long satisfiedCount = list.stream().filter(trace -> decider.isSatisfied(trace.getElapsed())).count();
        Long toleratedCount = list.stream().filter(trace -> decider.isTolerated(trace.getElapsed())).count();
        Long frustratedCount = list.stream().filter(trace -> decider.isFrustrated(trace.getElapsed())).count();

        ApplicationStatistic statistic = new ApplicationStatistic();
        statistic.setAppId(groupId);

        statistic.setMaxResponseTime(summaryStatistics.getMax());
        statistic.setSumResponseTime(summaryStatistics.getSum());
        statistic.setMinResponseTime(summaryStatistics.getMin());
        statistic.setPv(summaryStatistics.getCount());
        statistic.setError(errorCount);
        statistic.setSatisfied(satisfiedCount);
        statistic.setTolerated(toleratedCount);
        statistic.setFrustrated(frustratedCount);

        return statistic;
    }

}
