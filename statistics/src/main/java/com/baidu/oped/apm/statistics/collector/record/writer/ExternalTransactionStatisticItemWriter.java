package com.baidu.oped.apm.statistics.collector.record.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.ExternalServiceStatistic;
import com.baidu.oped.apm.common.jpa.entity.QExternalServiceStatistic;
import com.baidu.oped.apm.common.jpa.entity.StatisticType;
import com.baidu.oped.apm.common.jpa.repository.ExternalTransactionStatisticRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class ExternalTransactionStatisticItemWriter extends BaseWriter<ExternalServiceStatistic> {

    @Autowired
    private ExternalTransactionStatisticRepository externalTransactionStatisticRepository;

    @Override
    protected void writeItem(ExternalServiceStatistic item) {
        QExternalServiceStatistic qExternalTransactionStatistic = QExternalServiceStatistic.externalServiceStatistic;
        BooleanExpression appIdCondition =
                qExternalTransactionStatistic.externalServiceId.eq(item.getExternalServiceId());
        BooleanExpression periodCondition = qExternalTransactionStatistic.period.eq(item.getPeriod());
        BooleanExpression timestampCondition = qExternalTransactionStatistic.timestamp.eq(item.getTimestamp());

        BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
        ExternalServiceStatistic existStatistic = externalTransactionStatisticRepository.findOne(whereCondition);

        if (existStatistic == null) {
            externalTransactionStatisticRepository.save(item);
        } else {
            copyStatisticValue(item, existStatistic);
            externalTransactionStatisticRepository.saveAndFlush(existStatistic);
        }
    }

    @Override
    protected StatisticType getStatisticType() {
        return StatisticType.EXTERNAL_SERVICE;
    }
}
