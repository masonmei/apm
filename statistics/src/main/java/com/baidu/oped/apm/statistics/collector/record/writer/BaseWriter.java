package com.baidu.oped.apm.statistics.collector.record.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.CommonStatistic;
import com.baidu.oped.apm.common.jpa.entity.HostStatistic;
import com.baidu.oped.apm.common.jpa.entity.QStatisticState;
import com.baidu.oped.apm.common.jpa.entity.Statistic;
import com.baidu.oped.apm.common.jpa.entity.StatisticState;
import com.baidu.oped.apm.common.jpa.entity.StatisticType;
import com.baidu.oped.apm.common.jpa.repository.StatisticStateRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
public abstract class BaseWriter<T extends Statistic> {

    @Autowired
    protected StatisticStateRepository statisticStateRepository;

    /**
     * Write statistic items to database.
     *
     * @param items
     * @param periodStart
     * @param periodInMills
     */
    public void writeItems(Iterable<T> items, long periodStart, long periodInMills) {
        items.forEach(t -> {
            t.setTimestamp(periodStart);
            t.setPeriod(periodInMills);
            writeItem(t);
        });
        recordStatisticState(periodStart, periodInMills);
    }

    /**
     * Create or update a statistic item.
     *
     * @param item
     */
    protected abstract void writeItem(T item);

    /**
     * Copy non-null properties from object to object.
     *
     * @param from
     * @param to
     */
    protected void copyStatisticValue(T from, T to) {
        Assert.notNull(from, "Cannot copy statistic from null object.");
        Assert.notNull(to, "Cannot copy statistic to null object.");

        if (from instanceof CommonStatistic) {
            CommonStatistic baseFrom = (CommonStatistic) from;
            CommonStatistic baseTo = (CommonStatistic) to;

            copyBaseStatisticNotNullProperty(baseFrom, baseTo);
        }

        if (from instanceof HostStatistic) {
            HostStatistic hostFrom = (HostStatistic) from;
            HostStatistic hostTo = (HostStatistic) to;

            copyHostStatisticNotNullProperty(hostFrom, hostTo);
        }
    }

    private void copyHostStatisticNotNullProperty(HostStatistic hostFrom, HostStatistic hostTo) {
        Double cpuUsage = hostFrom.getCpuUsage();
        if (cpuUsage != null) {
            hostTo.setCpuUsage(cpuUsage);
        }

        Double memoryUsage = hostFrom.getMemoryUsage();
        if (memoryUsage != null) {
            hostTo.setMemoryUsage(memoryUsage);
        }
    }

    private void copyBaseStatisticNotNullProperty(CommonStatistic baseFrom, CommonStatistic baseTo) {
        Double sumResponseTime = baseFrom.getSumResponseTime();
        if (sumResponseTime != null) {
            baseTo.setSumResponseTime(sumResponseTime);
        }

        Double maxResponseTime = baseFrom.getMaxResponseTime();
        if (maxResponseTime != null) {
            baseTo.setMaxResponseTime(maxResponseTime);
        }

        Double minResponseTime = baseFrom.getMinResponseTime();
        if (minResponseTime != null) {
            baseTo.setMinResponseTime(minResponseTime);
        }

        Long pv = baseFrom.getPv();
        if (pv != null) {
            baseTo.setPv(pv);
        }

        Long error = baseFrom.getError();
        if (error != null) {
            baseTo.setError(error);
        }

        Long satisfied = baseFrom.getSatisfied();
        if (satisfied != null) {
            baseTo.setSatisfied(satisfied);
        }

        Long tolerated = baseFrom.getTolerated();
        if (tolerated != null) {
            baseTo.setTolerated(tolerated);
        }

        Long frustrated = baseFrom.getFrustrated();
        if (frustrated != null) {
            baseTo.setFrustrated(frustrated);
        }
    }

    protected void recordStatisticState(long periodStart, long periodInMills) {
        QStatisticState qStatisticState = QStatisticState.statisticState;
        BooleanExpression periodCondition = qStatisticState.period.eq(periodInMills);
        BooleanExpression statisticTypeCondition = qStatisticState.statisticType.eq(getStatisticType());
        BooleanExpression whereCondition = periodCondition.and(statisticTypeCondition);
        StatisticState one = statisticStateRepository.findOne(whereCondition);
        if(one == null){
            StatisticState statisticState = new StatisticState();
            statisticState.setStatisticType(getStatisticType());
            statisticState.setPeriod(periodInMills);
            statisticState.setTimestamp(periodStart);
            statisticStateRepository.save(statisticState);
        } else {
            one.setTimestamp(periodStart);
            statisticStateRepository.saveAndFlush(one);
        }
    }

    protected abstract StatisticType getStatisticType();
}
