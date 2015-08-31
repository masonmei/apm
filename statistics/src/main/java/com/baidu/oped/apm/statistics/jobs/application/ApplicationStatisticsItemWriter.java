package com.baidu.oped.apm.statistics.jobs.application;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.repository.ApplicationStatisticRepository;

/**
 * Created by mason on 8/29/15.
 */
@Component
public class ApplicationStatisticsItemWriter implements ItemWriter<ApplicationStatistic> {

    @Autowired
    private ApplicationStatisticRepository applicationStatisticRepository;

    @Override
    public void write(List<? extends ApplicationStatistic> items) throws Exception {
        applicationStatisticRepository.save(items);
    }
}
