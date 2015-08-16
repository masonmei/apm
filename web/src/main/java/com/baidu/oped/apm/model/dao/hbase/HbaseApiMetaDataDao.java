//
//package com.baidu.oped.apm.model.dao.hbase;
//
//import java.util.List;
//
//import org.apache.hadoop.hbase.client.Get;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.hadoop.hbase.RowMapper;
//import org.springframework.stereotype.Repository;
//
//import com.baidu.oped.apm.common.bo.ApiMetaDataBo;
//import com.baidu.oped.apm.common.hbase.HBaseTables;
//import com.baidu.oped.apm.common.hbase.HbaseOperations2;
//
//import com.baidu.oped.apm.model.dao.ApiMetaDataDao;
//import com.sematext.hbase.wd.RowKeyDistributorByHashPrefix;
//
///**
// * class HbaseApiMetaDataDao
// *
// * @author meidongxu@baidu.com
// */
//@Repository
//public class HbaseApiMetaDataDao implements ApiMetaDataDao {
//    static final String SPEL_KEY = "#agentId.toString() + '.' + #time.toString() + '.' + #apiId.toString()";
//
//
//    @Autowired
//    private HbaseOperations2 hbaseOperations2;
//
////    @Autowired
////    @Qualifier("apiMetaDataMapper")
//    private RowMapper<List<ApiMetaDataBo>> apiMetaDataMapper;
//
////    @Autowired
////    @Qualifier("metadataRowKeyDistributor")
//    private RowKeyDistributorByHashPrefix rowKeyDistributorByHashPrefix;
//
//    @Override
//    @Cacheable(value="apiMetaData", key=SPEL_KEY)
//    public List<ApiMetaDataBo> getApiMetaData(String agentId, long time, int apiId) {
//        if (agentId == null) {
//            throw new NullPointerException("agentId must not be null");
//        }
//
//        ApiMetaDataBo apiMetaDataBo = new ApiMetaDataBo(agentId, time, apiId);
//        byte[] sqlId = getDistributedKey(apiMetaDataBo.toRowKey());
//        Get get = new Get(sqlId);
//        get.addFamily(HBaseTables.API_METADATA_CF_API);
//
//        return hbaseOperations2.get(HBaseTables.API_METADATA, get, apiMetaDataMapper);
//    }
//
//    private byte[] getDistributedKey(byte[] rowKey) {
//        return rowKeyDistributorByHashPrefix.getDistributedKey(rowKey);
//    }
//}
