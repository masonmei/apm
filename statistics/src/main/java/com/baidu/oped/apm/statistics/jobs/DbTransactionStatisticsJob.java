package com.baidu.oped.apm.statistics.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;

/**
 * Created by mason on 8/29/15.
 */
public class DbTransactionStatisticsJob implements Job {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isRestartable() {
        return false;
    }

    @Override
    public void execute(JobExecution execution) {

    }

    @Override
    public JobParametersIncrementer getJobParametersIncrementer() {
        return null;
    }

    @Override
    public JobParametersValidator getJobParametersValidator() {
        return null;
    }
}
