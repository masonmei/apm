package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.oped.apm.common.jpa.entity.CommonStatistic;
import com.baidu.oped.apm.common.jpa.entity.TraceEvent;
import com.baidu.oped.apm.statistics.collector.ApdexDecider;

/**
 * Created by mason on 8/31/15.
 */
public abstract class BaseTraceEventProcessor<T extends CommonStatistic> extends BaseProcessor<TraceEvent, T> {

    @Autowired
    private ApdexDecider decider;

    public void setDecider(ApdexDecider decider) {
        this.decider = decider;
    }

    @Override
    public Iterable<T> process(Iterable<TraceEvent> items) {
        List<T> statistics = new ArrayList<>();
        Map<EventGroup, List<TraceEvent>> grouped = groupEvents(items);
        grouped.forEach((mappedKey, list) -> {
            statistics.add(buildStatistic(mappedKey, list));
        });

        return statistics;
    }

    private T buildStatistic(final EventGroup group, final List<TraceEvent> list) {
        DoubleSummaryStatistics summaryStatistics =
                list.stream().mapToDouble(value -> getElapsed(value)).summaryStatistics();
        Long errorCount = list.stream().filter(TraceEvent::isHasException).count();
        Long satisfiedCount =
                list.stream().filter(event -> decider.isSatisfied(getElapsed(event)))
                        .count();
        Long toleratedCount =
                list.stream().filter(event -> decider.isTolerated(getElapsed(event)))
                        .count();
        Long frustratedCount =
                list.stream().filter(event -> decider.isFrustrated(getElapsed(event)))
                        .count();
        T statistic = newStatisticInstance(group);

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

    private int getElapsed(TraceEvent event) {
        return Math.abs(event.getEndElapsed() - event.getStartElapsed());
    }

    protected abstract T newStatisticInstance(EventGroup group);

    protected abstract Map<EventGroup,List<TraceEvent>> groupEvents(Iterable<TraceEvent> items);

    interface EventGroup {
        Long getAppId();

        Long getInstanceId();
    }

}
