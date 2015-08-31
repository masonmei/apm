package com.baidu.oped.apm.statistics.collector.record.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.ExternalTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.QExternalTransactionStatistic;
import com.baidu.oped.apm.common.jpa.repository.ExternalTransactionStatisticRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class ExternalTransactionStatisticItemWriter extends BaseWriter<ExternalTransactionStatistic> {

    @Autowired
    private ExternalTransactionStatisticRepository externalTransactionStatisticRepository;

    protected ExternalTransactionStatisticItemWriter(long periodStart, long periodInMills) {
        super(periodStart, periodInMills);
    }

    @Override
    protected void writeItem(ExternalTransactionStatistic item) {
        QExternalTransactionStatistic qExternalTransactionStatistic =
                QExternalTransactionStatistic.externalTransactionStatistic;
        BooleanExpression appIdCondition =
                qExternalTransactionStatistic.externalTransactionId.eq(item.getExternalTransactionId());
        BooleanExpression periodCondition = qExternalTransactionStatistic.period.eq(item.getPeriod());
        BooleanExpression timestampCondition = qExternalTransactionStatistic.timestamp.eq(item.getTimestamp());

        BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
        ExternalTransactionStatistic existStatistic = externalTransactionStatisticRepository.findOne(whereCondition);

        if (existStatistic == null) {
            externalTransactionStatisticRepository.save(existStatistic);
        } else {
            copyStatisticValue(item, existStatistic);
            externalTransactionStatisticRepository.saveAndFlush(existStatistic);
        }
    }
}
