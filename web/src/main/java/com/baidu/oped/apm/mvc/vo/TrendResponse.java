package com.baidu.oped.apm.mvc.vo;

import java.util.List;

/**
 * Created by mason on 8/25/15.
 */
public class TrendResponse {
    private List<MetricVo> metrics;
    private List<MetricDataVo> values;

    public List<MetricVo> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricVo> metrics) {
        this.metrics = metrics;
    }

    public List<MetricDataVo> getValues() {
        return values;
    }

    public void setValues(List<MetricDataVo> values) {
        this.values = values;
    }
}
