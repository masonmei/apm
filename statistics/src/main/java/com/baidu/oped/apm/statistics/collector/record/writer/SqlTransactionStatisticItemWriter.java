package com.baidu.oped.apm.statistics.collector.record.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.QSqlTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.SqlTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.StatisticType;
import com.baidu.oped.apm.common.jpa.repository.SqlTransactionStatisticRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class SqlTransactionStatisticItemWriter extends BaseWriter<SqlTransactionStatistic> {

    @Autowired
    private SqlTransactionStatisticRepository sqlTransactionStatisticRepository;

    @Override
    protected void writeItem(SqlTransactionStatistic item) {
        QSqlTransactionStatistic qSqlTransactionStatistic = QSqlTransactionStatistic.sqlTransactionStatistic;
        BooleanExpression appIdCondition = qSqlTransactionStatistic.sqlTransactionId.eq(item.getSqlTransactionId());
        BooleanExpression periodCondition = qSqlTransactionStatistic.period.eq(item.getPeriod());
        BooleanExpression timestampCondition = qSqlTransactionStatistic.timestamp.eq(item.getTimestamp());

        BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
        SqlTransactionStatistic existStatistic = sqlTransactionStatisticRepository.findOne(whereCondition);

        if (existStatistic == null) {
            sqlTransactionStatisticRepository.save(item);
        } else {
            copyStatisticValue(item, existStatistic);
            sqlTransactionStatisticRepository.saveAndFlush(existStatistic);
        }
    }

    @Override
    protected StatisticType getStatisticType() {
        return StatisticType.DATABASE_SERVICE;
    }
}
