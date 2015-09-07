package com.baidu.oped.apm.mvc.vo;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.jpa.entity.Statistic;

/**
 * Created by mason on 9/6/15.
 */
public class TrendContext {
    private final Map<TimeRange, Map<ServiceType, Iterable<? extends Statistic>>> rangeServiceStatisticMap =
            new HashMap<>();
    private final Set<ServiceType> serviceTypes = new HashSet<>();
    private final Set<TimeRange> timeRanges = new HashSet<>();

    public TrendContext(TimeRange... timeRanges) {
        Arrays.stream(timeRanges).forEach(this.timeRanges::add);
    }

    public void addServiceData(ServiceType serviceType,
                               Map<TimeRange, Iterable<? extends Statistic>> serviceStatistics) {
        serviceTypes.add(serviceType);
        serviceStatistics.forEach((timeRange, statistics) -> {
            Map<ServiceType, Iterable<? extends Statistic>> serviceTypeStatisticMap =
                    rangeServiceStatisticMap.get(timeRange);
            if (serviceTypeStatisticMap == null) {
                rangeServiceStatisticMap.put(timeRange, new HashMap<>());
                serviceTypeStatisticMap = rangeServiceStatisticMap.get(timeRange);
            }
            serviceTypeStatisticMap.put(serviceType, statistics);
        });
    }

    public Set<TimeRange> getTimeRanges() {
        return timeRanges;
    }

    public Set<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public List<Statistic> getStatistic(TimeRange timeRange, ServiceType serviceType) {
        //TODO:
        return Collections.emptyList();
    }
}
