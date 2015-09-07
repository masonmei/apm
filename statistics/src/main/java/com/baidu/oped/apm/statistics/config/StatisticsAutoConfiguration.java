package com.baidu.oped.apm.statistics.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.scheduling.config.ContextLifecycleScheduledTaskRegistrar;

import com.baidu.oped.apm.statistics.collector.ApdexDecider;
import com.baidu.oped.apm.statistics.collector.DefaultApdexDecider;

/**
 * Created by mason on 8/29/15.
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(StatisticsAutoConfigurationProperties.class)
public class StatisticsAutoConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(StatisticsAutoConfiguration.class);

    @Autowired
    private StatisticsAutoConfigurationProperties properties;

    @Bean
    public ApdexDecider apdexDecider() {
        return new DefaultApdexDecider(500000l);
    }

    @Bean
    public ContextLifecycleScheduledTaskRegistrar taskRegistrar(){
        return new ContextLifecycleScheduledTaskRegistrar();
    }
}