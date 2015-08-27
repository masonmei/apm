package com.baidu.oped.apm.mvc.vo;

import java.util.List;

/**
 * Created by mason on 8/25/15.
 */
public class TrendResponse {
    private List<Metric> metrics;
    private List<MetricData> values;

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public List<MetricData> getValues() {
        return values;
    }

    public void setValues(List<MetricData> values) {
        this.values = values;
    }
}
