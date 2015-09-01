package com.baidu.oped.apm.statistics.collector.record.processor;

import com.baidu.oped.apm.common.jpa.entity.QSqlTransaction;
import com.baidu.oped.apm.common.jpa.entity.SqlTransactionStatistic;
import com.baidu.oped.apm.common.jpa.repository.SqlTransactionRepository;
import com.baidu.oped.apm.statistics.collector.ApdexDecider;

/**
 * Created by mason on 9/1/15.
 */
public class SqlTransactionProcessor extends BaseTraceEventProcessor<SqlTransactionStatistic> {
    private SqlTransactionRepository sqlTransactionRepository;

    public void setSqlTransactionRepository(SqlTransactionRepository sqlTransactionRepository) {
        this.sqlTransactionRepository = sqlTransactionRepository;
    }

    @Override
    public SqlTransactionStatistic newStatisticInstance(TransactionGroup group) {
        QSqlTransaction qSqlTransaction = QSqlTransaction.sqlTransaction;
//        qSqlTransaction.

        SqlTransactionStatistic statistic = new SqlTransactionStatistic();
        return statistic;
    }
}
