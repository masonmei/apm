package com.baidu.oped.apm.mvc.vo;

/**
 * Created by mason on 8/13/15.
 */
public class Metric {
    private String name;
    private String description;
    private String unit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
