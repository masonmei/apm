package com.baidu.oped.apm.statistics.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.ApplicationServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStat;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.statistics.collector.record.processor.AppServerStatisticProcessor;
import com.baidu.oped.apm.statistics.collector.record.processor.ApplicationHostStatProcessor;
import com.baidu.oped.apm.statistics.collector.record.processor.InstanceHostStatProcessor;
import com.baidu.oped.apm.statistics.collector.record.processor.InstanceServerStatisticProcessor;
import com.baidu.oped.apm.statistics.collector.record.reader.StatItemReader;
import com.baidu.oped.apm.statistics.collector.record.writer.AppServerStatisticItemWriter;
import com.baidu.oped.apm.statistics.collector.record.writer.ApplicationStatisticItemWriter;
import com.baidu.oped.apm.statistics.collector.record.writer.InstanceServerStatisticItemWriter;
import com.baidu.oped.apm.statistics.collector.record.writer.InstanceStatisticItemWriter;

/**
 * Created by mason on 9/1/15.
 */

@Component
public class StatCollector implements BaseCollector {

    @Autowired
    private StatItemReader reader;

    @Autowired
    private ApplicationHostStatProcessor applicationHostStatProcessor;

    @Autowired
    private ApplicationStatisticItemWriter applicationStatisticItemWriter;

    @Autowired
    private InstanceHostStatProcessor instanceHostStatProcessor;

    @Autowired
    private InstanceStatisticItemWriter instanceStatisticItemWriter;

    @Autowired
    private AppServerStatisticProcessor appServerStatisticProcessor;

    @Autowired
    private AppServerStatisticItemWriter appServerStatisticItemWriter;

    @Autowired
    private InstanceServerStatisticProcessor instanceServerStatisticProcessor;

    @Autowired
    private InstanceServerStatisticItemWriter instanceServerStatisticItemWriter;

    @Override
    public void collect(final long periodStart, final long periodInMills) {
        Iterable<InstanceStat> stats = reader.readItems(periodStart, periodInMills);

        Iterable<ApplicationStatistic> process = applicationHostStatProcessor.process(stats);
        applicationStatisticItemWriter.writeItems(process, periodStart, periodInMills);

        Iterable<InstanceStatistic> cpuStatus = instanceHostStatProcessor.process(stats);
        instanceStatisticItemWriter.writeItems(cpuStatus, periodStart, periodInMills);

        Iterable<ApplicationServerStatistic> applicationServerStatistics = appServerStatisticProcessor.process(stats);
        appServerStatisticItemWriter.writeItems(applicationServerStatistics, periodStart, periodInMills);

        Iterable<InstanceServerStatistic> instanceServerStatistics = instanceServerStatisticProcessor.process(stats);
        instanceServerStatisticItemWriter.writeItems(instanceServerStatistics, periodStart, periodInMills);
    }

    public void setReader(StatItemReader reader) {
        this.reader = reader;
    }

    public StatItemReader getReader() {
        return reader;
    }

    public ApplicationHostStatProcessor getApplicationHostStatProcessor() {
        return applicationHostStatProcessor;
    }

    public void setApplicationHostStatProcessor(ApplicationHostStatProcessor applicationHostStatProcessor) {
        this.applicationHostStatProcessor = applicationHostStatProcessor;
    }

    public ApplicationStatisticItemWriter getApplicationStatisticItemWriter() {
        return applicationStatisticItemWriter;
    }

    public void setApplicationStatisticItemWriter(ApplicationStatisticItemWriter applicationStatisticItemWriter) {
        this.applicationStatisticItemWriter = applicationStatisticItemWriter;
    }

    public InstanceHostStatProcessor getInstanceHostStatProcessor() {
        return instanceHostStatProcessor;
    }

    public void setInstanceHostStatProcessor(InstanceHostStatProcessor instanceHostStatProcessor) {
        this.instanceHostStatProcessor = instanceHostStatProcessor;
    }

    public InstanceStatisticItemWriter getInstanceStatisticItemWriter() {
        return instanceStatisticItemWriter;
    }

    public void setInstanceStatisticItemWriter(InstanceStatisticItemWriter instanceStatisticItemWriter) {
        this.instanceStatisticItemWriter = instanceStatisticItemWriter;
    }

    public AppServerStatisticProcessor getAppServerStatisticProcessor() {
        return appServerStatisticProcessor;
    }

    public void setAppServerStatisticProcessor(AppServerStatisticProcessor appServerStatisticProcessor) {
        this.appServerStatisticProcessor = appServerStatisticProcessor;
    }

    public AppServerStatisticItemWriter getAppServerStatisticItemWriter() {
        return appServerStatisticItemWriter;
    }

    public void setAppServerStatisticItemWriter(AppServerStatisticItemWriter appServerStatisticItemWriter) {
        this.appServerStatisticItemWriter = appServerStatisticItemWriter;
    }

    public InstanceServerStatisticProcessor getInstanceServerStatisticProcessor() {
        return instanceServerStatisticProcessor;
    }

    public void setInstanceServerStatisticProcessor(InstanceServerStatisticProcessor instanceServerStatisticProcessor) {
        this.instanceServerStatisticProcessor = instanceServerStatisticProcessor;
    }

    public InstanceServerStatisticItemWriter getInstanceServerStatisticItemWriter() {
        return instanceServerStatisticItemWriter;
    }

    public void setInstanceServerStatisticItemWriter(
            InstanceServerStatisticItemWriter instanceServerStatisticItemWriter) {
        this.instanceServerStatisticItemWriter = instanceServerStatisticItemWriter;
    }
}
