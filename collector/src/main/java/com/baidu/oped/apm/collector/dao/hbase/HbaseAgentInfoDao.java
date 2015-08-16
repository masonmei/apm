//
//package com.baidu.oped.apm.collector.dao.hbase;
//
//import com.baidu.oped.apm.collector.dao.AgentInfoDao;
//import com.baidu.oped.apm.collector.mapper.thrift.AgentInfoBoMapper;
//import com.baidu.oped.apm.collector.mapper.thrift.ServerMetaDataBoMapper;
//import com.baidu.oped.apm.collector.mapper.thrift.ThriftBoMapper;
//import com.baidu.oped.apm.common.bo.AgentInfoBo;
//import com.baidu.oped.apm.common.bo.ServerMetaDataBo;
//import com.baidu.oped.apm.common.hbase.HBaseTables;
//import com.baidu.oped.apm.common.hbase.HbaseOperations2;
//import com.baidu.oped.apm.common.util.RowKeyUtils;
//import com.baidu.oped.apm.common.util.TimeUtils;
//import com.baidu.oped.apm.thrift.dto.TAgentInfo;
//import com.baidu.oped.apm.thrift.dto.TServerMetaData;
//
//import org.apache.hadoop.hbase.client.Put;
//import org.apache.hadoop.hbase.util.Bytes;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//
///**
// * class HbaseAgentInfoDao
// *
// * @author meidongxu@baidu.com
// */
//@Repository
//public class HbaseAgentInfoDao implements AgentInfoDao {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private HbaseOperations2 hbaseTemplate;
//
//    @Autowired
//    private AgentInfoBoMapper agentInfoBoMapper;
//
//    @Autowired
//    private ServerMetaDataBoMapper serverMetaDataBoMapper;
//
//    @Override
//    public void insert(TAgentInfo agentInfo) {
//        if (agentInfo == null) {
//            throw new NullPointerException("agentInfo must not be null");
//        }
//
//        if (logger.isDebugEnabled()) {
//            logger.debug("insert agent info. {}", agentInfo);
//        }
//
//        byte[] agentId = Bytes.toBytes(agentInfo.getAgentId());
//        long reverseKey = TimeUtils.reverseTimeMillis(agentInfo.getStartTimestamp());
//        byte[] rowKey = RowKeyUtils.concatFixedByteAndLong(agentId, HBaseTables.AGENT_NAME_MAX_LEN, reverseKey);
//        Put put = new Put(rowKey);
//
//        // should add additional agent informations. for now added only starttime for sqlMetaData
//        AgentInfoBo agentInfoBo = this.agentInfoBoMapper.map(agentInfo);
//        byte[] agentInfoBoValue = agentInfoBo.writeValue();
//        put.addColumn(HBaseTables.AGENTINFO_CF_INFO, HBaseTables.AGENTINFO_CF_INFO_IDENTIFIER, agentInfoBoValue);
//
//        if (agentInfo.isSetServerMetaData()) {
//            ServerMetaDataBo serverMetaDataBo = this.serverMetaDataBoMapper.map(agentInfo.getServerMetaData());
//            byte[] serverMetaDataBoValue = serverMetaDataBo.writeValue();
//            put.addColumn(HBaseTables.AGENTINFO_CF_INFO, HBaseTables.AGENTINFO_CF_INFO_SERVER_META_DATA, serverMetaDataBoValue);
//        }
//
//        hbaseTemplate.put(HBaseTables.AGENTINFO, put);
//    }
//}
