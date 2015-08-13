package com.baidu.oped.apm.mvc.vo;

import java.util.List;

/**
 * Created by yangbolin on 15/8/13.
 */
public class LegendTrend {
    private String legend;
    private List<DataPoint> dataPoints;

    public LegendTrend() {
    }

    public LegendTrend(String legend, List<DataPoint> dataPoints) {
        this.legend = legend;
        this.dataPoints = dataPoints;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
