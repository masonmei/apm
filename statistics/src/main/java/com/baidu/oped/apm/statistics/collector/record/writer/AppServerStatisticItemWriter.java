package com.baidu.oped.apm.statistics.collector.record.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.ApplicationServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.QApplicationServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.StatisticType;
import com.baidu.oped.apm.common.jpa.repository.ApplicationServerStatisticRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 9/28/15.
 */
@Component
public class AppServerStatisticItemWriter extends BaseWriter<ApplicationServerStatistic> {

    @Autowired
    private ApplicationServerStatisticRepository applicationServerStatisticRepository;

    public void setApplicationServerStatisticRepository(
            ApplicationServerStatisticRepository applicationServerStatisticRepository) {
        this.applicationServerStatisticRepository = applicationServerStatisticRepository;
    }

    @Override
    protected void writeItem(ApplicationServerStatistic item) {
        QApplicationServerStatistic qApplicationServerStatistic =
                QApplicationServerStatistic.applicationServerStatistic;
        BooleanExpression appIdCondition = qApplicationServerStatistic.appId.eq(item.getAppId());
        BooleanExpression periodCondition = qApplicationServerStatistic.period.eq(item.getPeriod());
        BooleanExpression timestampCondition = qApplicationServerStatistic.timestamp.eq(item.getTimestamp());

        BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
        ApplicationServerStatistic existStatistic = applicationServerStatisticRepository.findOne(whereCondition);

        if (existStatistic == null) {
            applicationServerStatisticRepository.save(item);
        } else {
            copyStatisticValue(item, existStatistic);
            applicationServerStatisticRepository.saveAndFlush(existStatistic);
        }
    }

    @Override
    protected StatisticType getStatisticType() {
        return StatisticType.INSTANCE_STAT;
    }
}
