package com.baidu.oped.apm.statistics.collector;

import com.baidu.oped.apm.common.jpa.entity.AgentStatCpuLoad;
import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.statistics.collector.record.processor.ApplicationCpuProcessor;
import com.baidu.oped.apm.statistics.collector.record.processor.InstanceCpuProcessor;
import com.baidu.oped.apm.statistics.collector.record.reader.CpuStatItemReader;
import com.baidu.oped.apm.statistics.collector.record.writer.ApplicationStatisticItemWriter;
import com.baidu.oped.apm.statistics.collector.record.writer.InstanceStatisticItemWriter;

/**
 * Created by mason on 9/1/15.
 */
public class CpuCollector {
    private CpuStatItemReader reader;

    private ApplicationCpuProcessor applicationCpuProcessor;
    private InstanceCpuProcessor instanceCpuProcessor;

    private InstanceStatisticItemWriter instanceStatisticItemWriter;
    private ApplicationStatisticItemWriter applicationStatisticItemWriter;

    public void collect(long periodStart, long periodInMills) {
        reader = new CpuStatItemReader(periodStart, periodInMills);
        Iterable<AgentStatCpuLoad> cpuLoads = reader.readItems();

        applicationCpuProcessor = new ApplicationCpuProcessor();
        Iterable<ApplicationStatistic> process = applicationCpuProcessor.process(cpuLoads);

        applicationStatisticItemWriter = new ApplicationStatisticItemWriter(periodStart, periodInMills);
    }
}
