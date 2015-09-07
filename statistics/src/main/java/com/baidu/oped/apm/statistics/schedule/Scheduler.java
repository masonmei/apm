package com.baidu.oped.apm.statistics.schedule;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.QStatisticState;
import com.baidu.oped.apm.common.jpa.entity.StatisticState;
import com.baidu.oped.apm.common.jpa.entity.StatisticType;
import com.baidu.oped.apm.common.jpa.repository.InstanceStatRepository;
import com.baidu.oped.apm.common.jpa.repository.StatisticStateRepository;
import com.baidu.oped.apm.common.jpa.repository.TraceEventRepository;
import com.baidu.oped.apm.common.jpa.repository.TraceRepository;
import com.baidu.oped.apm.statistics.collector.DatabaseServiceCollector;
import com.baidu.oped.apm.statistics.collector.ExternalServiceCollector;
import com.baidu.oped.apm.statistics.collector.StatCollector;
import com.baidu.oped.apm.statistics.collector.TraceCollector;
import com.baidu.oped.apm.statistics.config.StatisticsAutoConfigurationProperties;
import com.baidu.oped.apm.statistics.utils.TimeUtil;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 9/5/15.
 */
@Component
public class Scheduler {

    @Autowired
    private StatisticStateRepository statisticStateRepository;

    @Autowired
    private StatisticsAutoConfigurationProperties properties;

    @Autowired
    private StatCollector statCollector;

    @Autowired
    private TraceCollector traceCollector;

    @Autowired
    private DatabaseServiceCollector databaseServiceCollector;

    @Autowired
    private ExternalServiceCollector externalServiceCollector;

    @Autowired
    private ScheduledTaskRegistrar taskRegistrar;

    @Autowired
    private InstanceStatRepository instanceStatRepository;

    @Autowired
    private TraceRepository traceRepository;

    @Autowired
    private TraceEventRepository traceEventRepository;

    @PostConstruct
    public void init() {
        if (properties.isFillStatistic()) {
            fillStatistic();
        } else {
            scheduleStatCollector();
            scheduleTraceCollector();
            scheduleDatabaseStatisticCollector();
            scheduleExternalStatisticCollector();
        }
    }

    private void fillStatistic() {
        List<Integer> periodsInSecond = properties.getPeriodsInSecond();
        periodsInSecond.stream().forEach(periodInSecond -> {
            // Collect stat statistics
            StatisticState state = findOrCreateStatisticState(StatisticType.INSTANCE_STAT, periodInSecond * 1000);
            long timestampInMillis = state.getTimestamp();
            while (!isCurrentPeriod(timestampInMillis, state.getPeriod())) {
                statCollector.collect(timestampInMillis, state.getPeriod());
                timestampInMillis += state.getPeriod();
            }
            taskRegistrar.addFixedRateTask(new StatisticTask<>(statCollector, periodInSecond));

            // Collect webTransaction statistic
            state = findOrCreateStatisticState(StatisticType.WEB_TRANSACTION, periodInSecond * 1000);
            timestampInMillis = state.getTimestamp();
            while (!isCurrentPeriod(timestampInMillis, state.getPeriod())) {
                traceCollector.collect(timestampInMillis, state.getPeriod());
                timestampInMillis += state.getPeriod();
            }
            taskRegistrar.addFixedRateTask(new StatisticTask<>(traceCollector, periodInSecond));

            //Collect databaseService statistic
            state = findOrCreateStatisticState(StatisticType.DATABASE_SERVICE, periodInSecond * 1000);
            timestampInMillis = state.getTimestamp();
            while (!isCurrentPeriod(timestampInMillis, state.getPeriod())) {
                databaseServiceCollector.collect(timestampInMillis, state.getPeriod());
                timestampInMillis += state.getPeriod();
            }
            taskRegistrar.addFixedRateTask(new StatisticTask<>(databaseServiceCollector, periodInSecond));

            // Collect externalService statistic
            state = findOrCreateStatisticState(StatisticType.EXTERNAL_SERVICE, periodInSecond * 1000);
            timestampInMillis = state.getTimestamp();
            while (!isCurrentPeriod(timestampInMillis, state.getPeriod())) {
                externalServiceCollector.collect(timestampInMillis, state.getPeriod());
                timestampInMillis += state.getPeriod();
            }
            taskRegistrar.addFixedDelayTask(new StatisticTask<>(externalServiceCollector, periodInSecond));
        });
    }

    private void scheduleStatCollector() {
        List<Integer> periodsInSecond = properties.getPeriodsInSecond();
        periodsInSecond.stream().filter(period -> period % 60 == 0)
                .forEach(period -> taskRegistrar.addFixedRateTask(new StatisticTask(statCollector, period)));
    }

    private void scheduleTraceCollector() {
        List<Integer> periodsInSecond = properties.getPeriodsInSecond();
        periodsInSecond.stream().filter(period -> period % 60 == 0)
                .forEach(period -> taskRegistrar.addFixedRateTask(new StatisticTask(traceCollector, period)));

    }

    private void scheduleDatabaseStatisticCollector() {
        List<Integer> periodsInSecond = properties.getPeriodsInSecond();
        periodsInSecond.stream().filter(period -> period % 60 == 0)
                .forEach(period -> taskRegistrar.addFixedRateTask(new StatisticTask(databaseServiceCollector, period)));

    }

    private void scheduleExternalStatisticCollector() {
        List<Integer> periodsInSecond = properties.getPeriodsInSecond();
        periodsInSecond.stream().filter(period -> period % 60 == 0)
                .forEach(period -> taskRegistrar.addFixedRateTask(new StatisticTask(externalServiceCollector, period)));

    }

    protected StatisticState findOrCreateStatisticState(StatisticType statisticType, long periodInMills) {
        QStatisticState qStatisticState = QStatisticState.statisticState;
        BooleanExpression statisticTypeCondition = qStatisticState.statisticType.eq(statisticType);
        BooleanExpression periodCondition = qStatisticState.period.eq(periodInMills);
        BooleanExpression whereCondition = statisticTypeCondition.and(periodCondition);
        StatisticState one = statisticStateRepository.findOne(whereCondition);
        if (one == null) {
            StatisticState statisticState = new StatisticState();
            statisticState.setStatisticType(statisticType);
            statisticState.setPeriod(periodInMills);
            calculateStartTime(statisticState);
            one = statisticStateRepository.saveAndFlush(statisticState);
        }
        return one;
    }

    protected void calculateStartTime(StatisticState statisticState) {
        Assert.notNull(statisticState, "StatisticState must not be null while calculate startTime");
        switch (statisticState.getStatisticType()) {
            case INSTANCE_STAT: {
                Long minTimestamp = instanceStatRepository.minTimestamp();
                long periodStart = getPeriodStart(minTimestamp, statisticState.getPeriod());
                statisticState.setTimestamp(periodStart);
                break;
            }
            case WEB_TRANSACTION: {
                Long minTimestamp = traceRepository.minTimestamp();
                long periodStart = getPeriodStart(minTimestamp, statisticState.getPeriod());
                statisticState.setTimestamp(periodStart);
                break;
            }
            case DATABASE_SERVICE: {
                Long minTimestamp = traceEventRepository.minTimestamp();
                long periodStart = getPeriodStart(minTimestamp, statisticState.getPeriod());
                statisticState.setTimestamp(periodStart);
                break;
            }
            case EXTERNAL_SERVICE: {
                long minTimestamp = traceEventRepository.minTimestamp();
                long periodStart = getPeriodStart(minTimestamp, statisticState.getPeriod());
                statisticState.setTimestamp(periodStart);
                break;
            }
            default:
                throw new UnsupportedOperationException("Not support yet.");
        }
    }

    protected long getPeriodStart(Long timestamp, Long periodInMills) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (timestamp != null && timestamp > 0) {
            localDateTime = new Timestamp(timestamp).toLocalDateTime();
        }

        LocalDateTime periodStart = TimeUtil.getPeriodStart(localDateTime, periodInMills, ChronoUnit.MILLIS);
        return periodStart.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    protected boolean isCurrentPeriod(long timestampInMillis,long periodInMillis) {
        LocalDateTime periodStart = TimeUtil.getPeriodStart(LocalDateTime.now(), periodInMillis, ChronoUnit.MILLIS);
        long currentPeriodStart = periodStart.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return timestampInMillis >= currentPeriodStart;
    }
}
