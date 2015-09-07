package com.baidu.oped.apm.statistics.schedule;

import org.springframework.scheduling.config.IntervalTask;

import com.baidu.oped.apm.statistics.collector.BaseCollector;

/**
 * Created by mason on 9/6/15.
 */
public class StatisticTask<T extends BaseCollector> extends IntervalTask {

    public StatisticTask(T collector, Integer period) {
        super(new StatisticRunnable<>(collector, period), period * 1000, 0);
    }

}
