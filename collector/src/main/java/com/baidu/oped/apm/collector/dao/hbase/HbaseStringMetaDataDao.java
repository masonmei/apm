
package com.baidu.oped.apm.collector.dao.hbase;

import com.baidu.oped.apm.collector.dao.StringMetaDataDao;
import com.baidu.oped.apm.common.bo.StringMetaDataBo;
import com.baidu.oped.apm.common.hbase.HBaseTables;
import com.baidu.oped.apm.common.hbase.HbaseOperations2;
import com.baidu.oped.apm.thrift.dto.TStringMetaData;
import com.sematext.hbase.wd.RowKeyDistributorByHashPrefix;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * class HbaseStringMetaDataDao 
 *
 * @author meidongxu@baidu.com
 */
@Repository
public class HbaseStringMetaDataDao implements StringMetaDataDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HbaseOperations2 hbaseTemplate;

    @Autowired
    @Qualifier("metadataRowKeyDistributor")
    private RowKeyDistributorByHashPrefix rowKeyDistributorByHashPrefix;

    @Override
    public void insert(TStringMetaData stringMetaData) {
        if (stringMetaData == null) {
            throw new NullPointerException("stringMetaData must not be null");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("insert:{}", stringMetaData);
        }

        final StringMetaDataBo stringMetaDataBo = new StringMetaDataBo(stringMetaData.getAgentId(), stringMetaData.getAgentStartTime(), stringMetaData.getStringId());
        final byte[] rowKey = getDistributedKey(stringMetaDataBo.toRowKey());


        Put put = new Put(rowKey);
        String stringValue = stringMetaData.getStringValue();
        byte[] sqlBytes = Bytes.toBytes(stringValue);
        put.addColumn(HBaseTables.STRING_METADATA_CF_STR, HBaseTables.STRING_METADATA_CF_STR_QUALI_STRING, sqlBytes);

        hbaseTemplate.put(HBaseTables.STRING_METADATA, put);
    }

    private byte[] getDistributedKey(byte[] rowKey) {
        return rowKeyDistributorByHashPrefix.getDistributedKey(rowKey);
    }
}
