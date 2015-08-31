package com.baidu.oped.apm.statistics.collector.record.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.ExternalTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.QExternalTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.QWebTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;
import com.baidu.oped.apm.common.jpa.repository.WebTransactionStatisticRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class WebTransactionStatisticItemWriter extends BaseWriter<WebTransactionStatistic> {

    @Autowired
    private WebTransactionStatisticRepository webTransactionStatisticRepository;

    protected WebTransactionStatisticItemWriter(long periodStart, long periodInMills) {
        super(periodStart, periodInMills);
    }

    @Override
    protected void writeItem(WebTransactionStatistic item) {
        QWebTransactionStatistic qWebTransactionStatistic =
                QWebTransactionStatistic.webTransactionStatistic;
        BooleanExpression appIdCondition =
                qWebTransactionStatistic.transactionId.eq(item.getTransactionId());
        BooleanExpression periodCondition = qWebTransactionStatistic.period.eq(item.getPeriod());
        BooleanExpression timestampCondition = qWebTransactionStatistic.timestamp.eq(item.getTimestamp());

        BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
        WebTransactionStatistic existStatistic = webTransactionStatisticRepository.findOne(whereCondition);

        if (existStatistic == null) {
            webTransactionStatisticRepository.save(existStatistic);
        } else {
            copyStatisticValue(item, existStatistic);
            webTransactionStatisticRepository.saveAndFlush(existStatistic);
        }
    }
}
