
package com.baidu.oped.apm.model.dao.hbase;

import static com.baidu.oped.apm.common.hbase.HBaseTables.AGENT_NAME_MAX_LEN;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Scan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.common.hbase.HBaseTables;
import com.baidu.oped.apm.common.hbase.HbaseOperations2;
import com.baidu.oped.apm.common.util.BytesUtils;
import com.baidu.oped.apm.common.util.RowKeyUtils;
import com.baidu.oped.apm.common.util.TimeUtils;

import com.baidu.oped.apm.model.dao.AgentStatDao;
import com.baidu.oped.apm.mvc.vo.AgentStat;
import com.baidu.oped.apm.mvc.vo.Range;
import com.sematext.hbase.wd.AbstractRowKeyDistributor;

/**
 * class HbaseAgentStatDao 
 *
 * @author meidongxu@baidu.com
 */
@Repository
public class HbaseAgentStatDao implements AgentStatDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HbaseOperations2 hbaseOperations2;

//    @Autowired
//    @Qualifier("agentStatMapper")
    private RowMapper<List<AgentStat>> agentStatMapper;

//    @Autowired
//    @Qualifier("agentStatRowKeyDistributor")
    private AbstractRowKeyDistributor rowKeyDistributor;

    private int scanCacheSize = 256;

    public void setScanCacheSize(int scanCacheSize) {
        this.scanCacheSize = scanCacheSize;
    }

    public List<AgentStat> scanAgentStatList(String agentId, Range range) {
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }
        if (range == null) {
            throw new NullPointerException("range must not be null");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("scanAgentStat : agentId={}, {}", agentId, range);
        }


        Scan scan = createScan(agentId, range);

        List<List<AgentStat>> intermediate = hbaseOperations2.find(HBaseTables.AGENT_STAT, scan, rowKeyDistributor, agentStatMapper);

        int expectedSize = (int)(range.getRange() / 5000); // data for 5 seconds
        List<AgentStat> merged = new ArrayList<AgentStat>(expectedSize);

        for(List<AgentStat> each : intermediate) {
            merged.addAll(each);
        }

        return merged;
    }

    /**
     * make a row key based on timestamp
     * FIXME there is the same duplicate code at collector's dao module
     */
    private byte[] getRowKey(String agentId, long timestamp) {
        if (agentId == null) {
            throw new IllegalArgumentException("agentId must not null");
        }
        byte[] bAgentId = BytesUtils.toBytes(agentId);
        return RowKeyUtils.concatFixedByteAndLong(bAgentId, AGENT_NAME_MAX_LEN, TimeUtils.reverseTimeMillis(timestamp));
    }

    private Scan createScan(String agentId, Range range) {
        Scan scan = new Scan();
        scan.setCaching(this.scanCacheSize);

        byte[] startKey = getRowKey(agentId, range.getFrom());
        byte[] endKey = getRowKey(agentId, range.getTo());

        // start key is replaced by end key because key has been reversed
        scan.setStartRow(endKey);
        scan.setStopRow(startKey);

        //        scan.addColumn(HBaseTables.AGENT_STAT_CF_STATISTICS, HBaseTables.AGENT_STAT_CF_STATISTICS_V1);
        scan.addFamily(HBaseTables.AGENT_STAT_CF_STATISTICS);
        scan.setId("AgentStatScan");

        // toString() method of Scan converts a message to json format so it is slow for the first time.
        logger.debug("create scan:{}", scan);
        return scan;
    }

    //    public List<AgentStat> scanAgentStatList(String agentId, long start, long end, final int limit) {
    //        if (logger.isDebugEnabled()) {
    //            logger.debug("scanAgentStatList");
    //        }
    //        Scan scan = createScan(agentId, start, end);
    //
    //        List<AgentStat> list = hbaseOperations2.find(HBaseTables.AGENT_STAT, scan, rowKeyDistributor, new ResultsExtractor<List<AgentStat>>() {
    //            @Override
    //            public List<AgentStat> extractData(ResultScanner results) throws Exception {
    //                TDeserializer deserializer = new TDeserializer();
    //                List<AgentStat> list = new ArrayList<AgentStat>();
    //                for (Result result : results) {
    //                    if (result == null) {
    //                        continue;
    //                    }
    //
    //                    if (list.size() >= limit) {
    //                        break;
    //                    }
    //
    //                    for (KeyValue kv : result.raw()) {
    //                        AgentStat agentStat = new AgentStat();
    //                        deserializer.deserialize(agentStat, kv.getBuffer());
    //                        list.add(agentStat);
    //                    }
    //                }
    //                return list;
    //            }
    //        });
    //        return list;
    //    }

}
