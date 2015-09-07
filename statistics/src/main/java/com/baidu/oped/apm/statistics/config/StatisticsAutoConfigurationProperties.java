package com.baidu.oped.apm.statistics.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by mason on 8/29/15.
 */
@ConfigurationProperties(prefix = "apm.statistics")
public class StatisticsAutoConfigurationProperties {
    private List<Integer> periodsInSecond;
    private boolean fillStatistic;

    public List<Integer> getPeriodsInSecond() {
        return periodsInSecond;
    }

    public void setPeriodsInSecond(List<Integer> periodsInSecond) {
        this.periodsInSecond = periodsInSecond;
    }

    public boolean isFillStatistic() {
        return fillStatistic;
    }

    public void setFillStatistic(boolean fillStatistic) {
        this.fillStatistic = fillStatistic;
    }
}
