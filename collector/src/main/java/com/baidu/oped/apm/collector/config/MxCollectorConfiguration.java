package com.baidu.oped.apm.collector.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;
import com.baidu.oped.apm.collector.dao.hbase.statistics.RowKeyMerge;
import com.baidu.oped.apm.collector.receiver.TcpDispatchHandler;
import com.baidu.oped.apm.collector.receiver.UdpDispatchHandler;
import com.baidu.oped.apm.collector.receiver.UdpSpanDispatchHandler;
import com.baidu.oped.apm.collector.receiver.tcp.TCPReceiver;
import com.baidu.oped.apm.collector.receiver.udp.BaseUDPReceiver;
import com.baidu.oped.apm.common.Version;
import com.baidu.oped.apm.common.hbase.HBaseTables;
import com.baidu.oped.apm.common.util.DefaultTimeSlot;
import com.baidu.oped.apm.common.util.TimeSlot;
import com.baidu.oped.apm.thrift.io.CommandHeaderTBaseDeserializerFactory;
import com.baidu.oped.apm.thrift.io.CommandHeaderTBaseSerializerFactory;

/**
 * Created by mason on 8/5/15.
 */
@Configuration
public class MxCollectorConfiguration {

    @Autowired
    private MxCollectorProperties collectorProperties;

    @Bean
    @Qualifier("callerMerge")
    RowKeyMerge callerMerge() {
        return new RowKeyMerge(HBaseTables.MAP_STATISTICS_CALLEE_CF_VER2_COUNTER);
    }

    @Bean
    @Qualifier("calleeMerge")
    RowKeyMerge calleeMerge() {
        return new RowKeyMerge(HBaseTables.MAP_STATISTICS_CALLER_CF_COUNTER);
    }

    @Bean
    @Qualifier("selfMerge")
    RowKeyMerge selfMerge() {
        return new RowKeyMerge(HBaseTables.MAP_STATISTICS_SELF_CF_COUNTER);
    }

    @Bean
    TimeSlot timeSlot() {
        return new DefaultTimeSlot();
    }

    @Bean
    @Qualifier("jsonObjectMapper")
    ObjectMapper jsonObjectMapper() {
        return new ObjectMapper();
    }
    //
    //    @Bean
    //    TcpDispatchHandler tcpDispatchHandler() {
    //        return new TcpDispatchHandler();
    //    }
    //
    //    @Bean
    //    @Qualifier("udpDispatchHandler")
    //    UdpDispatchHandler udpDispatchHandler() {
    //        return new UdpDispatchHandler();
    //    }

    //    @Bean
    //    @Qualifier("udpSpanDispatchHandler")
    //    UdpSpanDispatchHandler udpSpanDispatchHandler() {
    //        return new UdpSpanDispatchHandler();
    //    }

    //    @Bean(initMethod = "initialize", destroyMethod = "shutdown")
    //    AutoFlusher daoAutoFlusher() {
    //        AutoFlusher autoFlusher = new AutoFlusher();
    //        autoFlusher.setCachedStatisticsDaoList(
    //                  Arrays.asList(hbaseMapStatisticsCallerDao, hbaseMapStatisticsCalleeDao, hbaseMapResponseTimeDao)
    //        );
    //        autoFlusher.setFlushPeriod(collectorProperties.getFlushPeriod());
    //        return autoFlusher;
    //    }

    @Bean
    MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    //    @Bean
    //    CollectorMetric collectorMetric() {
    //        return new CollectorMetric();
    //    }

    @Bean
    @Qualifier("commandHeaderTBaseSerializerFactory")
    CommandHeaderTBaseSerializerFactory commandHeaderTBaseSerializerFactory() {
        return new CommandHeaderTBaseSerializerFactory(Version.VERSION);
    }

    @Bean
    @Qualifier("commandHeaderTBaseDeserializerFactory")
    CommandHeaderTBaseDeserializerFactory commandHeaderTBaseDeserializerFactory() {
        return new CommandHeaderTBaseDeserializerFactory(Version.VERSION);
    }

    @Bean
    @Qualifier("tcpReceiver")
    TCPReceiver tcpReceiver(TcpDispatchHandler tcpDispatchHandler) {
        TCPReceiver tcpReceiver = new TCPReceiver(tcpDispatchHandler,
                                                         collectorProperties.getTcpListenIp(),
                                                         collectorProperties.getTcpListenPort(),
                                                         collectorProperties.getL4ip());

        return tcpReceiver;
    }

    @Bean
    @Qualifier("udpSpanReceiver")
    BaseUDPReceiver udpSpanReceiver(UdpSpanDispatchHandler udpSpanDispatchHandler) {
        return new BaseUDPReceiver("Pinpoint-UDP-Span", udpSpanDispatchHandler,
                                          collectorProperties.getUdpSpanListenIp(),
                                          collectorProperties.getUdpSpanListenPort(),
                                          collectorProperties.getUdpSpanSocketReceiveBufferSize(),
                                          collectorProperties.getUdpSpanWorkerThread(),
                                          collectorProperties.getUdpSpanWorkerQueueSize());
    }

    @Bean
    @Qualifier("udpStatReceiver")
    BaseUDPReceiver udpStatReceiver(UdpDispatchHandler udpDispatchHandler) {
        return new BaseUDPReceiver("Pinpoint-UDP-Stat", udpDispatchHandler, collectorProperties.getUdpStatListenIp(),
                                          collectorProperties.getUdpStatListenPort(),
                                          collectorProperties.getUdpStatSocketReceiveBufferSize(),
                                          collectorProperties.getUdpStatWorkerThread(),
                                          collectorProperties.getUdpStatWorkerQueueSize());
    }

}
