package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.oped.apm.common.jpa.entity.BaseStatistic;
import com.baidu.oped.apm.common.jpa.entity.Trace;
import com.baidu.oped.apm.common.jpa.entity.TraceEvent;
import com.baidu.oped.apm.statistics.collector.ApdexDecider;

/**
 * Created by mason on 8/31/15.
 */
public abstract class BaseTraceEventProcessor<T extends BaseStatistic> extends BaseProcessor<TraceEvent, T> {

    @Autowired
    private ApdexDecider decider;

    public void setDecider(ApdexDecider decider) {
        this.decider = decider;
    }

    @Override
    public Iterable<T> process(Iterable<TraceEvent> items) {
        List<T> statistics = new ArrayList<>();
        Map<TransactionGroup, List<TraceEvent>> grouped =
                StreamSupport.stream(items.spliterator(), false)
                        .collect(Collectors.groupingBy(new Function<TraceEvent, TransactionGroup>() {
                            @Override
                            public TransactionGroup apply(TraceEvent t) {
                                TransactionGroup group = new TransactionGroup();
                                group.setAppId(t.getAppId());
                                group.setInstanceId(t.getInstanceId());
                                group.setRpc(t.getRpc());
                                return group;
                            }
                        }));
        grouped.forEach((instanceId, list) -> {
            DoubleSummaryStatistics summaryStatistics = list.stream()
                                             .mapToDouble(value -> value.getEndElapsed() - value.getStartElapsed())
                                             .summaryStatistics();
            Long errorCount = list.stream().filter(TraceEvent::isHasException).count();
            Long satisfiedCount =
                    list.stream()
                            .filter(event -> decider.isSatisfied(event.getEndElapsed() - event.getStartElapsed()))
                            .count();
            Long toleratedCount =
                    list.stream()
                            .filter(event -> decider.isTolerated(event.getEndElapsed() - event.getStartElapsed()))
                            .count();
            Long frustratedCount =
                    list.stream()
                            .filter(event -> decider.isFrustrated(event.getEndElapsed() - event.getStartElapsed()))
                            .count();

            T statistic = newStatisticInstance(instanceId);
            statistic.setMaxResponseTime(summaryStatistics.getMax());
            statistic.setSumResponseTime(summaryStatistics.getSum());
            statistic.setPv(summaryStatistics.getCount());
            statistic.setError(errorCount);
            statistic.setSatisfied(satisfiedCount);
            statistic.setTolerated(toleratedCount);
            statistic.setFrustrated(frustratedCount);

            statistics.add(statistic);
        });

        return statistics;
    }

    public abstract T newStatisticInstance(TransactionGroup group);



    class TransactionGroup {
        private Long appId;
        private Long instanceId;

        private String rpc;

        public Long getAppId() {
            return appId;
        }

        public void setAppId(Long appId) {
            this.appId = appId;
        }

        public Long getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(Long instanceId) {
            this.instanceId = instanceId;
        }

        public String getRpc() {
            return rpc;
        }

        public void setRpc(String rpc) {
            this.rpc = rpc;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TransactionGroup that = (TransactionGroup) o;

            if (appId != null ? !appId.equals(that.appId) : that.appId != null) {
                return false;
            }
            if (instanceId != null ? !instanceId.equals(that.instanceId) : that.instanceId != null) {
                return false;
            }
            return !(rpc != null ? !rpc.equals(that.rpc) : that.rpc != null);

        }

        @Override
        public int hashCode() {
            int result = appId != null ? appId.hashCode() : 0;
            result = 31 * result + (instanceId != null ? instanceId.hashCode() : 0);
            result = 31 * result + (rpc != null ? rpc.hashCode() : 0);
            return result;
        }
    }

}
