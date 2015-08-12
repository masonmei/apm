
package com.baidu.oped.apm.collector.dao.hbase;

import static com.baidu.oped.apm.common.hbase.HBaseTables.*;

import com.baidu.oped.apm.collector.dao.ApplicationIndexDao;
import com.baidu.oped.apm.common.hbase.HbaseOperations2;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * application names list.
 *
 * @author netspider
 * @author emeroad
 */
@Repository
public class HbaseApplicationIndexDao implements ApplicationIndexDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HbaseOperations2 hbaseTemplate;

    @Override
    public void insert(final TAgentInfo agentInfo) {
        if (agentInfo == null) {
            throw new NullPointerException("agentInfo must not be null");
        }

        Put put = new Put(Bytes.toBytes(agentInfo.getApplicationName()));
        byte[] qualifier = Bytes.toBytes(agentInfo.getAgentId());
        byte[] value = Bytes.toBytes(agentInfo.getServiceType());
        
        put.addColumn(APPLICATION_INDEX_CF_AGENTS, qualifier, value);
        
        hbaseTemplate.put(APPLICATION_INDEX, put);

        logger.debug("Insert agentInfo. {}", agentInfo);
    }
}
