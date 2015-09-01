package com.baidu.oped.apm.statistics.collector.record.processor;

import com.baidu.oped.apm.common.jpa.entity.ExternalTransactionStatistic;

/**
 * Created by mason on 9/1/15.
 */
public class ExternalTransactionProcessor extends BaseTraceEventProcessor<ExternalTransactionStatistic>{
    @Override
    public ExternalTransactionStatistic newStatisticInstance(TransactionGroup group) {
        ExternalTransactionStatistic statistic = new ExternalTransactionStatistic();
        return statistic;
    }
}
