package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.CommonStatistic;
import com.baidu.oped.apm.common.jpa.entity.Trace;
import com.baidu.oped.apm.statistics.collector.ApdexDecider;

/**
 * Created by mason on 8/31/15.
 */
public abstract class BaseTraceProcessor<T extends CommonStatistic> extends BaseProcessor<Trace, T> {

    @Autowired
    private ApdexDecider decider;

    public void setDecider(ApdexDecider decider) {
        this.decider = decider;
    }

    @Override
    public Iterable<T> process(Iterable<Trace> items) {
        List<T> statistics = new ArrayList<>();
        final Map<Long, AgentInstanceMap> maps = getAgentInstanceMaps(items);
        Map<WebTransactionGroup, List<Trace>> grouped = StreamSupport.stream(items.spliterator(), false)
                                                 .collect(Collectors.groupingBy(new Function<Trace, WebTransactionGroup>() {
                                                     @Override
                                                     public WebTransactionGroup apply(Trace t) {
                                                         WebTransactionGroup group = new WebTransactionGroup();
                                                         AgentInstanceMap map = maps.get(t.getAgentId());
                                                         group.setAppId(map.getAppId());
                                                         group.setInstanceId(map.getInstanceId());
                                                         group.setRpc(t.getRpc());
                                                         return group;
                                                     }
                                                 }));
        grouped.forEach((mappedKey, list) -> {
            DoubleSummaryStatistics summaryStatistics = list.stream()
                                             .mapToDouble(Trace::getElapsed)
                                             .summaryStatistics();
            Long errorCount = list.stream().filter(trace -> trace.getErrCode() == 1).count();
            Long satisfiedCount = list.stream().filter(trace -> decider.isSatisfied(trace.getElapsed())).count();
            Long toleratedCount = list.stream().filter(trace -> decider.isTolerated(trace.getElapsed())).count();
            Long frustratedCount = list.stream().filter(trace -> decider.isFrustrated(trace.getElapsed())).count();

            T statistic = findOrCreateStatisticInstance(mappedKey);
            statistic.setMaxResponseTime(summaryStatistics.getMax());
            statistic.setSumResponseTime(summaryStatistics.getSum());
            statistic.setMinResponseTime(summaryStatistics.getMin());
            statistic.setPv(summaryStatistics.getCount());
            statistic.setError(errorCount);
            statistic.setSatisfied(satisfiedCount);
            statistic.setTolerated(toleratedCount);
            statistic.setFrustrated(frustratedCount);

            statistics.add(statistic);
        });

        return statistics;
    }

    public abstract T findOrCreateStatisticInstance(WebTransactionGroup mappedKey);

    class WebTransactionGroup {
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

            WebTransactionGroup that = (WebTransactionGroup) o;

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
