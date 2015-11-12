package com.baidu.oped.apm.collector.config;

import com.baidu.oped.apm.collector.receiver.udp.TBaseFilterChain;
import com.baidu.oped.apm.common.Version;
import com.baidu.oped.apm.common.util.AgentEventMessageSerializer;
import com.baidu.oped.apm.common.util.DefaultTimeSlot;
import com.baidu.oped.apm.common.util.ExecutorFactory;
import com.baidu.oped.apm.common.util.TimeSlot;
import com.baidu.oped.apm.thrift.io.CommandHeaderTBaseDeserializerFactory;
import com.baidu.oped.apm.thrift.io.CommandHeaderTBaseSerializerFactory;
import com.baidu.oped.apm.thrift.io.HeaderTBaseSerializer;
import com.baidu.oped.apm.thrift.io.SerializerFactory;
import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by mason on 8/5/15.
 */
@Configuration
@EnableConfigurationProperties(MxCollectorProperties.class)
public class MxCollectorConfiguration {

    @Autowired
    private MxCollectorProperties collectorProperties;

    @Bean
    TimeSlot timeSlot() {
        return new DefaultTimeSlot();
    }


    @Bean
    MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Bean
    CommandHeaderTBaseSerializerFactory commandHeaderTBaseSerializerFactory() {
        return new CommandHeaderTBaseSerializerFactory(Version.VERSION);
    }

    @Bean
    public AgentEventMessageSerializer agentEventMessageSerializer(CommandHeaderTBaseSerializerFactory commandHeaderTBaseSerializerFactory) {
        return new AgentEventMessageSerializer(commandHeaderTBaseSerializerFactory);
    }

    @Bean
    CommandHeaderTBaseDeserializerFactory commandHeaderTBaseDeserializerFactory() {
        return new CommandHeaderTBaseDeserializerFactory(Version.VERSION);
    }

    @Bean
    public TBaseFilterChain filterChain() {
        //TODO: add filters
        return new TBaseFilterChain(Collections.emptyList());
    }

    @Bean
    public TcpConfig tcpConfig() {
        return collectorProperties.getTcpConfig();
    }

    @Bean
    public SpanConfig spanConfig() {
        return collectorProperties.getSpanConfig();
    }

    @Bean
    public StatConfig statConfig() {
        return collectorProperties.getStatConfig();
    }

    @Bean
    @Qualifier("agentEventWorker")
    public ThreadPoolExecutor executorFactory() {
        return ExecutorFactory.newFixedThreadPool(
                collectorProperties.getAgentEventWorkerThread(),
                collectorProperties.getAgentEventWorkerQueueSize(),
                "AgentEvent-Worker",
                true);
    }

}
