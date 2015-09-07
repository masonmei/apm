package com.baidu.oped.apm.statistics.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.Trace;
import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;
import com.baidu.oped.apm.statistics.collector.record.processor.ApplicationCommonStatProcessor;
import com.baidu.oped.apm.statistics.collector.record.processor.InstanceCommonStatProcessor;
import com.baidu.oped.apm.statistics.collector.record.processor.WebTransactionProcessor;
import com.baidu.oped.apm.statistics.collector.record.reader.TraceItemReader;
import com.baidu.oped.apm.statistics.collector.record.writer.ApplicationStatisticItemWriter;
import com.baidu.oped.apm.statistics.collector.record.writer.InstanceStatisticItemWriter;
import com.baidu.oped.apm.statistics.collector.record.writer.WebTransactionStatisticItemWriter;

/**
 * Created by mason on 9/2/15.
 */
@Component
public class TraceCollector implements BaseCollector {
    @Autowired
    private TraceItemReader traceItemReader;

    @Autowired
    private WebTransactionProcessor webTransactionProcessor;

    @Autowired
    private ApplicationCommonStatProcessor applicationCommonStatProcessor;

    @Autowired
    private InstanceCommonStatProcessor instanceCommonStatProcessor;

    @Autowired
    private WebTransactionStatisticItemWriter webTransactionStatisticItemWriter;

    @Autowired
    private ApplicationStatisticItemWriter applicationStatisticItemWriter;

    @Autowired
    private InstanceStatisticItemWriter instanceStatisticItemWriter;


    @Override
    public void collect(long periodStart, long periodInMills) {
        Iterable<Trace> traces = traceItemReader.readItems(periodStart, periodInMills);

        Iterable<ApplicationStatistic> process = applicationCommonStatProcessor.process(traces);
        applicationStatisticItemWriter.writeItems(process, periodStart, periodInMills);

        Iterable<InstanceStatistic> instanceStatistics = instanceCommonStatProcessor.process(traces);
        instanceStatisticItemWriter.writeItems(instanceStatistics, periodStart, periodInMills);

        Iterable<WebTransactionStatistic> webTransactionStatistics = webTransactionProcessor.process(traces);
        webTransactionStatisticItemWriter.writeItems(webTransactionStatistics, periodStart, periodInMills);
    }

    public void setTraceItemReader(TraceItemReader traceItemReader) {
        this.traceItemReader = traceItemReader;
    }

    public void setWebTransactionProcessor(WebTransactionProcessor webTransactionProcessor) {
        this.webTransactionProcessor = webTransactionProcessor;
    }

    public void setApplicationCommonStatProcessor(ApplicationCommonStatProcessor applicationCommonStatProcessor) {
        this.applicationCommonStatProcessor = applicationCommonStatProcessor;
    }

    public void setInstanceCommonStatProcessor(InstanceCommonStatProcessor instanceCommonStatProcessor) {
        this.instanceCommonStatProcessor = instanceCommonStatProcessor;
    }

    public void setWebTransactionStatisticItemWriter(WebTransactionStatisticItemWriter
                                                             webTransactionStatisticItemWriter) {
        this.webTransactionStatisticItemWriter = webTransactionStatisticItemWriter;
    }

    public void setApplicationStatisticItemWriter(ApplicationStatisticItemWriter applicationStatisticItemWriter) {
        this.applicationStatisticItemWriter = applicationStatisticItemWriter;
    }

    public void setInstanceStatisticItemWriter(InstanceStatisticItemWriter instanceStatisticItemWriter) {
        this.instanceStatisticItemWriter = instanceStatisticItemWriter;
    }
}
