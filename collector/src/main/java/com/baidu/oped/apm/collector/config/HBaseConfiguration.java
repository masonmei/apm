package com.baidu.oped.apm.collector.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mason on 8/5/15.
 */
@Configuration
public class HBaseConfiguration {
    @Autowired
    private HBaseProperties hBaseProperties;

//    @Bean
//    HbaseConfigurationFactoryBean hbaseConfiguration() {
//        HbaseConfigurationFactoryBean factoryBean = new HbaseConfigurationFactoryBean();
//
//        Properties properties = new Properties();
//        //TODO
//        properties.setProperty("hbase.zookeeper.quorum", hBaseProperties.getClientHost());
//        properties.setProperty("hbase.zookeeper.property.clientPort", hBaseProperties.getClientPort());
//        properties.setProperty("hbase.ipc.client.tcpnodelay", hBaseProperties.isClientTcpNodelay() + "");
//        properties.setProperty("hbase.rpc.timeout", hBaseProperties.getRpcTimeout() + "");
//        properties.setProperty("hbase.client.operation.timeout", hBaseProperties.getOperationTimeout() + "");
//        properties.setProperty("hbase.ipc.client.socket.timeout.read", hBaseProperties.getSocketReadTimeout() + "");
//        properties.setProperty("hbase.ipc.client.socket.timeout.write", hBaseProperties.getSocketWriteTimeout() + "");
//        factoryBean.setProperties(properties);
//        factoryBean.setDeleteConnection(false);
//        return factoryBean;
//    }
//
//    @Bean
//    PooledHTableFactory connectionFactory(HbaseConfigurationFactoryBean hbaseConfiguration) {
//        return new PooledHTableFactory(hbaseConfiguration.getObject(), hBaseProperties.getMaxThreadSize(),
//                                              hBaseProperties.getPoolQueueSize(), hBaseProperties.isPrestartPool());
//    }
//
//    @Bean
//    HbaseTemplate2 hbaseTemplate(HbaseConfigurationFactoryBean hbaseConfiguration,
//                                 PooledHTableFactory connectionFactory) {
//        HbaseTemplate2 template2 = new HbaseTemplate2(hbaseConfiguration.getObject());
//        template2.setTableFactory(connectionFactory);
//        return template2;
//    }
//
//    @Bean(destroyMethod = "close")
//    HBaseAdminTemplate hBaseAdminTemplate(HbaseConfigurationFactoryBean hbaseConfiguration) {
//        return new HBaseAdminTemplate(hbaseConfiguration.getObject());
//    }
//
//    @Bean
//    @Qualifier("applicationTraceIndexDistributor")
//    RowKeyDistributorByHashPrefix applicationTraceIndexDistributor(){
//        return new RowKeyDistributorByHashPrefix(new RowKeyDistributorByHashPrefix.OneByteSimpleHash(32));
//    }
//
//    @Bean
//    @Qualifier("traceDistributor")
//    RowKeyDistributorByHashPrefix traceDistributor(){
//        return new RowKeyDistributorByHashPrefix(new RowKeyDistributorByHashPrefix.OneByteSimpleHash(64));
//    }
//
//    @Bean
//    @Qualifier("agentStatRowKeyDistributor")
//    RowKeyDistributorByHashPrefix agentStatRowKeyDistributor(){
//        return new RowKeyDistributorByHashPrefix(new RangeOneByteSimpleHash(0, 24, 32));
//    }
//
//    @Bean
//    @Qualifier("metadataRowKeyDistributor")
//    RowKeyDistributorByHashPrefix metadataRowKeyDistributor(){
//        return new RowKeyDistributorByHashPrefix(new RangeOneByteSimpleHash(0, 32, 8));
//    }
//
//    @Bean
//    @Qualifier("metadataRowKeyDistributor2")
//    RowKeyDistributorByHashPrefix metadataRowKeyDistributor2(){
//        return new RowKeyDistributorByHashPrefix(new RangeOneByteSimpleHash(0, 36, 32));
//    }
//
//    @Bean
//    @Qualifier("acceptApplicationRowKeyDistributor")
//    RowKeyDistributorByHashPrefix acceptApplicationRowKeyDistributor(){
//        return new RowKeyDistributorByHashPrefix(new RangeOneByteSimpleHash(0, 24, 4));
//    }
}
