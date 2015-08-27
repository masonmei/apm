package com.baidu.oped.apm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.mvc.vo.Metric;
import com.baidu.oped.apm.mvc.vo.MetricData;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TrendResponse;

/**
 * Created by mason on 8/27/15.
 */
public class MetricUtils {
    public static TrendResponse toTrendResponse(Map<TimeRange, Iterable<ApplicationStatistic>> statistics,
                                                Constaints.MetricName... metricNames) {
        Assert.notNull(metricNames, "MetricNames must not be null while convert to trendResponse.");
        Assert.notNull(statistics, "Statistics must not be null while convert to trendResponse.");

        TrendResponse trendResponse = new TrendResponse();
        List<Metric> metrics = new ArrayList<>();
        for (Constaints.MetricName metricName : metricNames) {
            Metric metric = new Metric();
            metric.setDescription(metricName.getDescription());
            metric.setName(metricName.getFieldName());
            metric.setUnit(metricName.getUnit());
            metrics.add(metric);
        }
        trendResponse.setMetrics(metrics);

        List<MetricData> values = new ArrayList<>();

        for (Map.Entry<TimeRange, Iterable<ApplicationStatistic>> entry : statistics.entrySet()) {
            MetricData metricData = new MetricData();
            //TODO:
            metricData.setTime(entry.getKey().toString());
//            metricData.setLegend();
            values.add(metricData);
        }

        trendResponse.setValues(values);
        return trendResponse;
    }
}
