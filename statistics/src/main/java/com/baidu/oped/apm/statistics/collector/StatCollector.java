package com.baidu.oped.apm.statistics.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStat;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.statistics.collector.record.processor.ApplicationHostStatProcessor;
import com.baidu.oped.apm.statistics.collector.record.processor.InstanceHostStatProcessor;
import com.baidu.oped.apm.statistics.collector.record.reader.StatItemReader;
import com.baidu.oped.apm.statistics.collector.record.writer.ApplicationStatisticItemWriter;
import com.baidu.oped.apm.statistics.collector.record.writer.InstanceStatisticItemWriter;

/**
 * Created by mason on 9/1/15.
 */

@Component
public class StatCollector implements BaseCollector {

    @Autowired
    private StatItemReader reader;

    @Autowired
    private ApplicationHostStatProcessor applicationCpuProcessor;

    @Autowired
    private InstanceHostStatProcessor instanceCpuProcessor;

    @Autowired
    private InstanceStatisticItemWriter instanceStatisticItemWriter;

    @Autowired
    private ApplicationStatisticItemWriter applicationStatisticItemWriter;

    @Override
    public void collect(final long periodStart, final long periodInMills) {
        Iterable<InstanceStat> stats = reader.readItems(periodStart, periodInMills);

        Iterable<ApplicationStatistic> process = applicationCpuProcessor.process(stats);
        applicationStatisticItemWriter.writeItems(process, periodStart, periodInMills);

        Iterable<InstanceStatistic> cpuStatus = instanceCpuProcessor.process(stats);
        instanceStatisticItemWriter.writeItems(cpuStatus, periodStart, periodInMills);
    }

    public void setReader(StatItemReader reader) {
        this.reader = reader;
    }

    public void setApplicationCpuProcessor(ApplicationHostStatProcessor applicationCpuProcessor) {
        this.applicationCpuProcessor = applicationCpuProcessor;
    }

    public void setInstanceCpuProcessor(InstanceHostStatProcessor instanceCpuProcessor) {
        this.instanceCpuProcessor = instanceCpuProcessor;
    }

    public void setInstanceStatisticItemWriter(InstanceStatisticItemWriter instanceStatisticItemWriter) {
        this.instanceStatisticItemWriter = instanceStatisticItemWriter;
    }

    public void setApplicationStatisticItemWriter(ApplicationStatisticItemWriter applicationStatisticItemWriter) {
        this.applicationStatisticItemWriter = applicationStatisticItemWriter;
    }
}
