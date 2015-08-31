package com.baidu.oped.apm.statistics.config;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.baidu.oped.apm.statistics.jobs.ApplicationStatisticsJob;
import com.baidu.oped.apm.statistics.jobs.DbTransactionStatisticsJob;
import com.baidu.oped.apm.statistics.jobs.ExternalTransactionStatisticsJob;
import com.baidu.oped.apm.statistics.jobs.InstanceStatisticsJob;
import com.baidu.oped.apm.statistics.jobs.SqlTransactionStatisticsJob;
import com.baidu.oped.apm.statistics.jobs.WebTransactionStatisticsJob;

/**
 * Created by mason on 8/29/15.
 */
@Configuration
@EnableScheduling
public class StatisticsConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(StatisticsConfiguration.class);

    @Autowired
    private StatisticsAutoConfigurationProperties properties;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job starCatalogExtractJob;

    @Autowired
    private ApplicationStatisticsJob applicationStatisticsJob;

    @Autowired
    private InstanceStatisticsJob instanceStatisticsJob;

    @Autowired
    private WebTransactionStatisticsJob webTransactionStatisticsJob;

    @Autowired
    private DbTransactionStatisticsJob dbTransactionStatisticsJob;

    @Autowired
    private SqlTransactionStatisticsJob sqlTransactionStatisticsJob;

    @Autowired
    private ExternalTransactionStatisticsJob externalTransactionStatisticsJob;

    @Scheduled(fixedDelayString = "${apm.statistics.webTransaction.fixedDelay}",
               initialDelayString = "${apm.statistics.webTransaction.initialDelay}")
    public void startWebTransactionStatistics()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
                           JobInstanceAlreadyCompleteException {
        LOG.info("start startWebTransactionStatistics at {}", LocalDateTime.now());
        JobParameters jobParameters =
                new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(webTransactionStatisticsJob, jobParameters);
    }

    @Scheduled(fixedDelayString = "${apm.statistics.externalTransaction.fixedDelay}",
               initialDelayString = "${apm.statistics.externalTransaction.initialDelay}")
    public void startExternalTransactionStatistics()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
                           JobInstanceAlreadyCompleteException {
        LOG.info("start startExternalTransactionStatistics at {}", LocalDateTime.now());
        JobParameters jobParameters =
                new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(externalTransactionStatisticsJob, jobParameters);
    }

    @Scheduled(fixedDelayString = "${apm.statistics.sqlTransaction.fixedDelay}",
               initialDelayString = "${apm.statistics.sqlTransaction.initialDelay}")
    public void startSqlTransactionStatistics()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
                           JobInstanceAlreadyCompleteException {
        LOG.info("start startSqlTransactionStatistics at {}", LocalDateTime.now());
        JobParameters jobParameters =
                new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(sqlTransactionStatisticsJob, jobParameters);
    }

    @Scheduled(fixedDelayString = "${apm.statistics.dbTransaction.fixedDelay}",
               initialDelayString = "${apm.statistics.dbTransaction.initialDelay}")
    public void startDbTransactionStatistics()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
                           JobInstanceAlreadyCompleteException {
        LOG.info("start startDbTransactionStatistics at {}", LocalDateTime.now());
        JobParameters jobParameters =
                new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(dbTransactionStatisticsJob, jobParameters);
    }

    @Scheduled(fixedDelayString = "${apm.statistics.application.fixedDelay}",
               initialDelayString = "${apm.statistics.application.initialDelay}")
    public void startApplicationStatistics()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
                           JobInstanceAlreadyCompleteException {
        LOG.info("start startApplicationStatistics at {}", LocalDateTime.now());
        JobParameters jobParameters =
                new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(applicationStatisticsJob, jobParameters);
    }

    @Scheduled(fixedDelayString = "${apm.statistics.instance.fixedDelay}",
               initialDelayString = "${apm.statistics.instance.initialDelay}")
    public void startInstanceStatistics()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
                           JobInstanceAlreadyCompleteException {
        LOG.info("start startInstanceStatistics at {}", LocalDateTime.now());
        JobParameters jobParameters =
                new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(instanceStatisticsJob, jobParameters);
    }
}