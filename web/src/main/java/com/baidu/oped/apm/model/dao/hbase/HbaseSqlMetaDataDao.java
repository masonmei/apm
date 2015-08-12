
package com.baidu.oped.apm.model.dao.hbase;

import java.util.List;

import org.apache.hadoop.hbase.client.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.RowMapper;

import com.baidu.oped.apm.common.bo.SqlMetaDataBo;
import com.baidu.oped.apm.common.hbase.HBaseTables;
import com.baidu.oped.apm.common.hbase.HbaseOperations2;

import com.baidu.oped.apm.model.dao.SqlMetaDataDao;
import com.sematext.hbase.wd.RowKeyDistributorByHashPrefix;

/**
 * class HbaseSqlMetaDataDao 
 *
 * @author meidongxu@baidu.com
 */
//@Repository
public class HbaseSqlMetaDataDao implements SqlMetaDataDao {

    @Autowired
    private HbaseOperations2 hbaseOperations2;

//    @Autowired
//    @Qualifier("sqlMetaDataMapper2")
    private RowMapper<List<SqlMetaDataBo>> sqlMetaDataMapper;

//    @Autowired
//    @Qualifier("metadataRowKeyDistributor2")
    private RowKeyDistributorByHashPrefix rowKeyDistributorByHashPrefix;

    @Override
    public List<SqlMetaDataBo> getSqlMetaData(String agentId, long time, int hashCode) {
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }

        SqlMetaDataBo sqlMetaData = new SqlMetaDataBo(agentId, time, hashCode);
        byte[] sqlId = getDistributedKey(sqlMetaData.toRowKey());

        Get get = new Get(sqlId);
        get.addFamily(HBaseTables.SQL_METADATA_VER2_CF_SQL);

        return hbaseOperations2.get(HBaseTables.SQL_METADATA_VER2, get, sqlMetaDataMapper);
    }

    private byte[] getDistributedKey(byte[] rowKey) {
        return rowKeyDistributorByHashPrefix.getDistributedKey(rowKey);
    }
    
    public void setSqlMetaDataMapper(RowMapper<List<SqlMetaDataBo>> sqlMetaDataMapper) {
		this.sqlMetaDataMapper = sqlMetaDataMapper;
	}
    
    public void setRowKeyDistributorByHashPrefix(RowKeyDistributorByHashPrefix rowKeyDistributorByHashPrefix) {
		this.rowKeyDistributorByHashPrefix = rowKeyDistributorByHashPrefix;
	}
}
