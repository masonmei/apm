package com.baidu.oped.apm.mvc.vo;

import java.util.List;

/**
 * Created by mason on 8/13/15.
 */
public class DataPoint {
    private long timestamp;
    private List<Integer> items;

    public DataPoint() {
    }

    public DataPoint(long timestamp, List<Integer> items) {
        this.timestamp = timestamp;
        this.items = items;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }
}
