package com.baidu.oped.apm.mvc.vo;

import java.util.List;

/**
 * Created by mason on 8/13/15.
 */
public class DataPoint {
    private long timestamp;
    private List<Double> items;

    public DataPoint() {
    }

    public DataPoint(long timestamp, List<Double> items) {
        this.timestamp = timestamp;
        this.items = items;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Double> getItems() {
        return items;
    }

    public void setItems(List<Double> items) {
        this.items = items;
    }
}
