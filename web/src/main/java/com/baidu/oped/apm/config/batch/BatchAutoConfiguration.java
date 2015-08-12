package com.baidu.oped.apm.config.batch;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mason on 8/10/15.
 */
//@Configuration
public class BatchAutoConfiguration {
    @Bean
    public MapJobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    @Bean
    public JobRegistryBeanPostProcessor postProcessor(JobRegistry jobRegistry){
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }

    @Bean
    public JobRepository jobRepository(){
//        return new SimpleJobRepository();
        return null;
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRegistry){
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRegistry);
        return launcher;
    }

}
