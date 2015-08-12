
package com.baidu.oped.apm.batch;

import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

/**
 * class BatchJobLauncher 
 *
 * @author meidongxu@baidu.com
 */
//@Component
public class BatchJobLauncher extends JobLaunchSupport {
    public void alarmJob() {
        JobParameters params = createTimeParameter();
        run("alarmJob", params);
    }

    private JobParameters createTimeParameter() {
        JobParametersBuilder builder = new JobParametersBuilder();
        Date now = new Date();
        builder.addDate("schedule.date", now);
        return builder.toJobParameters();
    }
}
