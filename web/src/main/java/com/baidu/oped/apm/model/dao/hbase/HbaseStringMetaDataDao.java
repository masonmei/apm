//
//package com.baidu.oped.apm.model.dao.hbase;
//
//import java.util.List;
//
//import org.apache.hadoop.hbase.client.Get;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.hadoop.hbase.RowMapper;
//import org.springframework.stereotype.Repository;
//
//import com.baidu.oped.apm.common.bo.StringMetaDataBo;
//import com.baidu.oped.apm.common.hbase.HBaseTables;
//import com.baidu.oped.apm.common.hbase.HbaseOperations2;
//
//import com.baidu.oped.apm.model.dao.StringMetaDataDao;
//import com.sematext.hbase.wd.RowKeyDistributorByHashPrefix;
//
///**
// * class HbaseStringMetaDataDao
// *
// * @author meidongxu@baidu.com
// */
//@Repository
//public class HbaseStringMetaDataDao implements StringMetaDataDao {
//
//    @Autowired
//    private HbaseOperations2 hbaseOperations2;
//
////    @Autowired
////    @Qualifier("stringMetaDataMapper")
//    private RowMapper<List<StringMetaDataBo>> stringMetaDataMapper;
//
////    @Autowired
////    @Qualifier("metadataRowKeyDistributor")
//    private RowKeyDistributorByHashPrefix rowKeyDistributorByHashPrefix;
//
//    @Override
//    public List<StringMetaDataBo> getStringMetaData(String agentId, long time, int stringId) {
//        if (agentId == null) {
//            throw new NullPointerException("agentId must not be null");
//        }
//
//        StringMetaDataBo stringMetaData = new StringMetaDataBo(agentId, time, stringId);
//        byte[] rowKey = getDistributedKey(stringMetaData.toRowKey());
//
//        Get get = new Get(rowKey);
//        get.addFamily(HBaseTables.STRING_METADATA_CF_STR);
//
//        return hbaseOperations2.get(HBaseTables.STRING_METADATA, get, stringMetaDataMapper);
//    }
//
//    private byte[] getDistributedKey(byte[] rowKey) {
//        return rowKeyDistributorByHashPrefix.getDistributedKey(rowKey);
//    }
//}
