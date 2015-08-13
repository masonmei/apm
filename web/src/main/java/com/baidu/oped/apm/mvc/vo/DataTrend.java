package com.baidu.oped.apm.mvc.vo;

import java.util.List;

/**
 * Created by yangbolin on 15/8/13.
 */
public class DataTrend {

    private List<Metric> metrics;
    private List<LegendTrend> legendTrends;


    public DataTrend() {}

    public DataTrend(List<Metric> metrics, List<LegendTrend> legendTrends) {
        this.metrics = metrics;
        this.legendTrends = legendTrends;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public List<LegendTrend> getLegendTrends() {
        return legendTrends;
    }

    public void setLegendTrends(List<LegendTrend> legendTrends) {
        this.legendTrends = legendTrends;
    }
}
