package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStat;

/**
 * Created by mason on 8/31/15.
 */
public class ApplicationHostStatProcessorTest {
    private List<InstanceStat> items;
    private ApplicationHostStatProcessor cpuProcessor;

    private Random random = new Random(System.currentTimeMillis());

    @Before
    public void setUp() throws Exception {
        cpuProcessor = new ApplicationHostStatProcessor();
        items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            InstanceStat cpuLoad = new InstanceStat();
            cpuLoad.setJvmCpuLoad(random.nextDouble());
            cpuLoad.setAppId(i % 3l + 1);
            items.add(cpuLoad);
        }
    }

    @Test
    public void testProcess() throws Exception {


        Iterable<ApplicationStatistic> process = cpuProcessor.process(items);
        process.forEach(applicationStatistic -> System.out.println(
                    String.format("%s:%s", applicationStatistic.getAppId(), applicationStatistic.getCpuUsage()))
        );
    }
}