
package com.baidu.oped.apm.collector.dao.hbase;

import com.baidu.oped.apm.collector.dao.SqlMetaDataDao;
import com.baidu.oped.apm.common.bo.SqlMetaDataBo;
import com.baidu.oped.apm.common.hbase.HBaseTables;
import com.baidu.oped.apm.common.hbase.HbaseOperations2;
import com.baidu.oped.apm.thrift.dto.TSqlMetaData;
import com.sematext.hbase.wd.RowKeyDistributorByHashPrefix;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * class HbaseSqlMetaDataDao 
 *
 * @author meidongxu@baidu.com
 */
@Repository("hbaseSqlMetaDataDao")
public class HbaseSqlMetaDataDao implements SqlMetaDataDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HbaseOperations2 hbaseTemplate;

    @Autowired
    @Qualifier("metadataRowKeyDistributor2")
    private RowKeyDistributorByHashPrefix rowKeyDistributorByHashPrefix;

    @Override
    public void insert(TSqlMetaData sqlMetaData) {
        if (sqlMetaData == null) {
            throw new NullPointerException("sqlMetaData must not be null");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("insert:{}", sqlMetaData);
        }

        SqlMetaDataBo sqlMetaDataBo = new SqlMetaDataBo(sqlMetaData.getAgentId(), sqlMetaData.getAgentStartTime(), sqlMetaData.getSqlId());
        final byte[] rowKey = getDistributedKey(sqlMetaDataBo.toRowKey());


        Put put = new Put(rowKey);
        String sql = sqlMetaData.getSql();
        byte[] sqlBytes = Bytes.toBytes(sql);

        put.addColumn(HBaseTables.SQL_METADATA_VER2_CF_SQL, HBaseTables.SQL_METADATA_VER2_CF_SQL_QUALI_SQLSTATEMENT, sqlBytes);

        hbaseTemplate.put(HBaseTables.SQL_METADATA_VER2, put);
    }

    private byte[] getDistributedKey(byte[] rowKey) {
        return rowKeyDistributorByHashPrefix.getDistributedKey(rowKey);
    }
}
