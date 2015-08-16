//
//package com.baidu.oped.apm.collector.dao.hbase;
//
//import com.baidu.oped.apm.collector.dao.ApiMetaDataDao;
//import com.baidu.oped.apm.common.bo.ApiMetaDataBo;
//import com.baidu.oped.apm.common.buffer.AutomaticBuffer;
//import com.baidu.oped.apm.common.buffer.Buffer;
//import com.baidu.oped.apm.common.hbase.HBaseTables;
//import com.baidu.oped.apm.common.hbase.HbaseOperations2;
//import com.baidu.oped.apm.thrift.dto.TApiMetaData;
//import com.sematext.hbase.wd.RowKeyDistributorByHashPrefix;
//
//import org.apache.hadoop.hbase.client.Put;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//
///**
// * class HbaseApiMetaDataDao
// *
// * @author meidongxu@baidu.com
// */
//@Repository
//public class HbaseApiMetaDataDao implements ApiMetaDataDao {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private HbaseOperations2 hbaseTemplate;
//
//    @Autowired
//    @Qualifier("metadataRowKeyDistributor")
//    private RowKeyDistributorByHashPrefix rowKeyDistributorByHashPrefix;
//
//    @Override
//    public void insert(TApiMetaData apiMetaData) {
//        if (logger.isDebugEnabled()) {
//            logger.debug("insert:{}", apiMetaData);
//        }
//
//
//        ApiMetaDataBo apiMetaDataBo = new ApiMetaDataBo(apiMetaData.getAgentId(), apiMetaData.getAgentStartTime(), apiMetaData.getApiId());
//        byte[] rowKey = getDistributedKey(apiMetaDataBo.toRowKey());
//
//        final Put put = new Put(rowKey);
//
//        final Buffer buffer = new AutomaticBuffer(64);
//        String api = apiMetaData.getApiInfo();
//        buffer.putPrefixedString(api);
//        if (apiMetaData.isSetLine()) {
//            buffer.put(apiMetaData.getLine());
//        } else {
//            buffer.put(-1);
//        }
//        final byte[] apiMetaDataBytes = buffer.getBuffer();
//        put.addColumn(HBaseTables.API_METADATA_CF_API, HBaseTables.API_METADATA_CF_API_QUALI_SIGNATURE, apiMetaDataBytes);
//
//        hbaseTemplate.put(HBaseTables.API_METADATA, put);
//    }
//
//    private byte[] getDistributedKey(byte[] rowKey) {
//        return rowKeyDistributorByHashPrefix.getDistributedKey(rowKey);
//    }
//}
