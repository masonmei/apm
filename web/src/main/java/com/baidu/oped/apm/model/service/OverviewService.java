package com.baidu.oped.apm.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.utils.Constaints;

/**
 * Created by mason on 8/27/15.
 */
@Service
public class OverviewService {

    @Autowired
    private ApplicationStatistic applicationStatistic;

    /**
     * Return the given metrics of application level.
     *
     * @param timeRanges
     * @param period
     * @param metricNames
     *
     * @return
     */
    public Map<TimeRange, Iterable<ApplicationStatistic>> getApplicationMetricData(
              Long appId, List<TimeRange> timeRanges, Integer period, Constaints.MetricName... metricNames) {
        return null;
    }
}
