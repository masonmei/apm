package com.baidu.oped.apm.statistics.collector.record.processor;

import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;

/**
 * Created by mason on 9/1/15.
 */
public class WebTransactionProcessor extends BaseTraceProcessor<WebTransactionStatistic> {

    @Override
    public WebTransactionStatistic newStatisticInstance() {
        return new WebTransactionStatistic();
    }
}
