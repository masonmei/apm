package com.baidu.oped.apm.statistics.collector.record.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.QApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.StatisticType;
import com.baidu.oped.apm.common.jpa.repository.ApplicationStatisticRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class ApplicationStatisticItemWriter extends BaseWriter<ApplicationStatistic> {

    @Autowired
    private ApplicationStatisticRepository applicationStatisticRepository;

    @Override
    protected void writeItem(ApplicationStatistic item) {
        QApplicationStatistic qApplicationStatistic = QApplicationStatistic.applicationStatistic;
        BooleanExpression appIdCondition = qApplicationStatistic.appId.eq(item.getAppId());
        BooleanExpression periodCondition = qApplicationStatistic.period.eq(item.getPeriod());
        BooleanExpression timestampCondition = qApplicationStatistic.timestamp.eq(item.getTimestamp());

        BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
        ApplicationStatistic existStatistic = applicationStatisticRepository.findOne(whereCondition);

        if (existStatistic == null) {
            applicationStatisticRepository.save(item);
        } else {
            copyStatisticValue(item, existStatistic);
            applicationStatisticRepository.saveAndFlush(existStatistic);
        }
    }

    @Override
    protected StatisticType getStatisticType() {
        return StatisticType.INSTANCE_STAT;
    }

}
