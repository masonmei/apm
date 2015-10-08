package com.baidu.oped.apm.mvc.vo;

import java.util.List;

/**
 * Created by mason on 8/25/15.
 */
public class MetricDataVo {
    private String legend;
    private String time;
    private List<DataPointVo> data;

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<DataPointVo> getData() {
        return data;
    }

    public void setData(List<DataPointVo> data) {
        this.data = data;
    }
}
