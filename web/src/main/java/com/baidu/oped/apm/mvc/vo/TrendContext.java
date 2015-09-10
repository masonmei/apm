package com.baidu.oped.apm.mvc.vo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baidu.oped.apm.common.jpa.entity.ExternalServiceStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.jpa.entity.SqlTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.Statistic;
import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;
import com.baidu.oped.apm.common.utils.TimeUtil;
import com.baidu.oped.apm.model.entity.DummyStatistic;

/**
 * Created by mason on 9/6/15.
 */
public class TrendContext<T> {
    private final Map<TimeRange, Map<T, Iterable<? extends Statistic>>> rangeServiceStatisticMap =
            new HashMap<>();
    private final Set<T> serviceTypes = new HashSet<>();
    private final Set<TimeRange> timeRanges = new HashSet<>();
    private final Long periodInMillis;

    public TrendContext(Long periodInMillis, TimeRange... timeRanges) {
        Arrays.stream(timeRanges).forEach(this.timeRanges::add);
        this.periodInMillis = periodInMillis;
    }

    public void addWebTransactionData(T serviceType,
            Map<TimeRange, Iterable<WebTransactionStatistic>> serviceStatistics) {
        serviceStatistics.forEach((timeRange, statistics) -> addStatistics(serviceType, timeRange, statistics));
    }

    public void addDatabaseServiceData(T serviceType,
            Map<TimeRange, Iterable<SqlTransactionStatistic>> serviceStatistics) {
        serviceStatistics.forEach((timeRange, statistics) -> addStatistics(serviceType, timeRange, statistics));
    }

    public void addExternalServiceData(T serviceType,
            Map<TimeRange, Iterable<ExternalServiceStatistic>> serviceStatistics) {
        serviceStatistics.forEach((timeRange, statistics) -> addStatistics(serviceType, timeRange, statistics));
    }

    public void addStatistics(T serviceType, TimeRange timeRange, Iterable<? extends Statistic> statistics) {
        serviceTypes.add(serviceType);
        Map<T, Iterable<? extends Statistic>> serviceTypeStatisticMap = rangeServiceStatisticMap.get(timeRange);
        if (serviceTypeStatisticMap == null) {
            rangeServiceStatisticMap.put(timeRange, new HashMap<>());
            serviceTypeStatisticMap = rangeServiceStatisticMap.get(timeRange);
        }
        serviceTypeStatisticMap.put(serviceType, statistics);
    }

    public Set<TimeRange> getTimeRanges() {
        return timeRanges;
    }

    public Set<T> getServiceTypes() {
        return serviceTypes;
    }

    public List<Statistic> getStatistic(TimeRange timeRange, T serviceType) {
        Iterable<? extends Statistic> statistics = rangeServiceStatisticMap.get(timeRange).get(serviceType);
        Map<Long, Statistic> timestampStatisticMap = new HashMap<>();
        statistics.forEach(statistic -> timestampStatisticMap.put(statistic.getTimestamp(), statistic));
        List<Long> timestamps = TimeUtil.getTimestamps(timeRange.getFrom(), timeRange.getTo(), periodInMillis);

        List<Statistic> statisticList = timestamps.stream().map(timestamp -> {
            Statistic statistic = timestampStatisticMap.get(timestamp);
            if (statistic == null) {
                statistic = new DummyStatistic();
                statistic.setTimestamp(timestamp);
                statistic.setPeriod(periodInMillis);
            }
            return statistic;
        }).collect(Collectors.toList());

        return statisticList;
    }
}
