package com.baidu.oped.apm.statistics.schedule;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import com.baidu.oped.apm.common.utils.TimeUtil;
import com.baidu.oped.apm.statistics.collector.BaseCollector;


/**
 * Created by mason on 9/6/15.
 */
public class StatisticRunnable<T extends BaseCollector> implements Runnable {
    private final T collector;
    private final long periodInSecond;

    public StatisticRunnable(T collector, long periodInSecond) {
        this.collector = collector;
        this.periodInSecond = periodInSecond;
    }

    @Override
    public void run() {
        collector.collect(getPeriodStart(), periodInSecond * 1000);
    }

    private long getPeriodStart() {
        LocalDateTime periodStartTime =
                TimeUtil.getPeriodStart(LocalDateTime.now(), periodInSecond, ChronoUnit.SECONDS);
        return periodStartTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
