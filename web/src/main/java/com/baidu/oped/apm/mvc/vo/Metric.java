package com.baidu.oped.apm.mvc.vo;

/**
 * Created by mason on 8/13/15.
 */
public class Metric {
    private String name;
    private String unit;
    private double value;

    public Metric() {
    }

    public Metric(String name, String unit, double value) {
        this.name = name;
        this.unit = unit;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
