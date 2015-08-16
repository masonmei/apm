//
//package com.baidu.oped.apm.collector.dao.hbase;
//
//import static com.baidu.oped.apm.common.hbase.HBaseTables.*;
//
//import org.apache.hadoop.hbase.client.Put;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//
//import com.baidu.oped.apm.collector.dao.AgentStatDao;
//import com.baidu.oped.apm.collector.mapper.thrift.AgentStatCpuLoadBoMapper;
//import com.baidu.oped.apm.collector.mapper.thrift.AgentStatMemoryGcBoMapper;
//import com.baidu.oped.apm.collector.mapper.thrift.ThriftBoMapper;
//import com.baidu.oped.apm.common.bo.AgentStatCpuLoadBo;
//import com.baidu.oped.apm.common.bo.AgentStatMemoryGcBo;
//import com.baidu.oped.apm.common.hbase.HbaseOperations2;
//import com.baidu.oped.apm.common.util.BytesUtils;
//import com.baidu.oped.apm.common.util.RowKeyUtils;
//import com.baidu.oped.apm.common.util.TimeUtils;
//import com.baidu.oped.apm.thrift.dto.TAgentStat;
//import com.sematext.hbase.wd.AbstractRowKeyDistributor;
//
///**
// * class HbaseAgentStatDao
// *
// * @author meidongxu@baidu.com
// */
//@Repository
//public class HbaseAgentStatDao implements AgentStatDao {
//
//    @Autowired
//    private HbaseOperations2 hbaseTemplate;
//
//    @Autowired
//    private AgentStatMemoryGcBoMapper agentStatMemoryGcBoMapper;
//
//    @Autowired
//    private AgentStatCpuLoadBoMapper agentStatCpuLoadBoMapper;
//
//    @Autowired
//    @Qualifier("agentStatRowKeyDistributor")
//    private AbstractRowKeyDistributor rowKeyDistributor;
//
//    public void insert(final TAgentStat agentStat) {
//        if (agentStat == null) {
//            throw new NullPointerException("agentStat must not be null");
//        }
//        long timestamp = agentStat.getTimestamp();
//        byte[] key = getDistributedRowKey(agentStat, timestamp);
//
//        Put put = new Put(key);
//
//        final AgentStatMemoryGcBo agentStatMemoryGcBo = this.agentStatMemoryGcBoMapper.map(agentStat);
//        put.addColumn(AGENT_STAT_CF_STATISTICS, AGENT_STAT_CF_STATISTICS_MEMORY_GC, timestamp, agentStatMemoryGcBo.writeValue());
//
//        final AgentStatCpuLoadBo agentStatCpuLoadBo = this.agentStatCpuLoadBoMapper.map(agentStat);
//        put.addColumn(AGENT_STAT_CF_STATISTICS, AGENT_STAT_CF_STATISTICS_CPU_LOAD, timestamp, agentStatCpuLoadBo.writeValue());
//
//        hbaseTemplate.put(AGENT_STAT, put);
//    }
//
//    /**
//     * Create row key based on the timestamp
//     */
//    private byte[] getRowKey(String agentId, long timestamp) {
//        if (agentId == null) {
//            throw new IllegalArgumentException("agentId must not null");
//        }
//        byte[] bAgentId = BytesUtils.toBytes(agentId);
//        return RowKeyUtils.concatFixedByteAndLong(bAgentId, AGENT_NAME_MAX_LEN, TimeUtils.reverseTimeMillis(timestamp));
//    }
//
//    /**
//     * Create row key based on the timestamp and distribute it into different buckets
//     */
//    private byte[] getDistributedRowKey(TAgentStat agentStat, long timestamp) {
//        byte[] key = getRowKey(agentStat.getAgentId(), timestamp);
//        return rowKeyDistributor.getDistributedKey(key);
//    }
//
//}
