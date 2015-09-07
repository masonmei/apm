package com.baidu.oped.apm.statistics.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.ExternalServiceStatistic;
import com.baidu.oped.apm.common.jpa.entity.TraceEvent;
import com.baidu.oped.apm.statistics.collector.record.processor.ExternalServiceProcessor;
import com.baidu.oped.apm.statistics.collector.record.reader.ExternalServiceItemReader;
import com.baidu.oped.apm.statistics.collector.record.writer.ExternalTransactionStatisticItemWriter;

/**
 * Created by mason on 9/2/15.
 */
@Component
public class ExternalServiceCollector implements BaseCollector {

    @Autowired
    private ExternalServiceItemReader externalServiceItemReader;

    @Autowired
    private ExternalTransactionStatisticItemWriter externalTransactionStatisticItemWriter;

    @Autowired
    private ExternalServiceProcessor externalServiceProcessor;

    @Override
    public void collect(long periodStart, long periodInMills) {
        Iterable<TraceEvent> traceEvents = externalServiceItemReader.readItems(periodStart, periodInMills);

        Iterable<ExternalServiceStatistic> externalTransactionStatistics =
                externalServiceProcessor.process(traceEvents);
        externalTransactionStatisticItemWriter.writeItems(externalTransactionStatistics, periodStart, periodInMills);
    }

    public void setExternalServiceItemReader(ExternalServiceItemReader externalServiceItemReader) {
        this.externalServiceItemReader = externalServiceItemReader;
    }

    public void setExternalTransactionStatisticItemWriter(ExternalTransactionStatisticItemWriter
                                                                  externalTransactionStatisticItemWriter) {
        this.externalTransactionStatisticItemWriter = externalTransactionStatisticItemWriter;
    }

    public void setExternalServiceProcessor(ExternalServiceProcessor externalServiceProcessor) {
        this.externalServiceProcessor = externalServiceProcessor;
    }
}
