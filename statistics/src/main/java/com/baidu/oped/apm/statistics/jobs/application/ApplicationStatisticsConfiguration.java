package com.baidu.oped.apm.statistics.jobs.application;

import static java.lang.String.format;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mason on 8/29/15.
 */
@Configuration
public class ApplicationStatisticsConfiguration {
    private static final String JOB_NAME = "ApplicationStatisticsJob";
    private static final String START_STEP = "startStep";
    private static final String FINISH_STEP = "finishedStep";
    private static final String REAL_WORK_STEP = "realWorkStep";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private Tasklet startTasklet;

    @Autowired
    private Tasklet finishTasklet;

    @Autowired
    private Tasklet realWorkTasklet;

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                       .start(startStep(startTasklet))
                       .next(realWorkStep(realWorkTasklet))
                       .next(finishedStep(finishTasklet))
                       .build();
    }

    @Bean
    private Step startStep(Tasklet tasklet){
        return stepBuilderFactory.get(format("%s-%s", JOB_NAME, START_STEP)).tasklet(tasklet).build();
    }

    @Bean
    private Step realWorkStep(Tasklet realWorkTasklet) {
        return stepBuilderFactory.get(format("%s-%s", JOB_NAME, REAL_WORK_STEP)).tasklet(realWorkTasklet).build();
    }

    @Bean
    private Step finishedStep(Tasklet tasklet){
        return stepBuilderFactory.get(format("%s-%s", JOB_NAME, FINISH_STEP)).tasklet(tasklet).build();
    }
}
