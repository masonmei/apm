package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.BaseRepository;
import com.baidu.oped.apm.collector.dao.ApplicationTraceIndexDao;
import com.baidu.oped.apm.collector.util.AcceptedTimeService;
import com.baidu.oped.apm.common.entity.ApplicationTraceIndex;
import com.baidu.oped.apm.thrift.dto.TSpan;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcApplicationTraceIndexDao extends BaseRepository<ApplicationTraceIndex> implements
        ApplicationTraceIndexDao {
    @Autowired
    private AcceptedTimeService acceptedTimeService;

    @Override
    public void insert(TSpan span) {
        if (span == null) {
            throw new NullPointerException("span must not be null");
        }

//        final Buffer buffer = new AutomaticBuffer(10 + AGENT_NAME_MAX_LEN);
//        buffer.putVar(span.getElapsed());
//        buffer.putSVar(span.getErr());
//        buffer.putPrefixedString(span.getAgentId());
//        final byte[] value = buffer.getBuffer();
//
//        long acceptedTime = acceptedTimeService.getAcceptedTime();
//        final byte[] distributedKey = crateRowKey(span, acceptedTime);
//        Put put = new Put(distributedKey);
//
//        put.addColumn(APPLICATION_TRACE_INDEX_CF_TRACE, makeQualifier(span), acceptedTime, value);
//
//        hbaseTemplate.put(APPLICATION_TRACE_INDEX, put);
    }
}


//    @Autowired
//    @Qualifier("applicationTraceIndexDistributor")
//    private AbstractRowKeyDistributor rowKeyDistributor;
//
//    @Override
//    public void insert(final TSpan span) {

//    }
//
//    private byte[] makeQualifier(final TSpan span) {
//        boolean useIndexedQualifier = false;
//        byte[] qualifier;
//
//        if (useIndexedQualifier) {
//            final Buffer columnName = new AutomaticBuffer(16);
//            // FIXME putVar not used in order to utilize hbase column prefix filter
//            columnName.put(span.getElapsed());
//            columnName.put(SpanUtils.getVarTransactionId(span));
//            qualifier = columnName.getBuffer();
//        } else {
//            // OLD
//            // byte[] transactionId = SpanUtils.getTransactionId(span);
//            qualifier = SpanUtils.getVarTransactionId(span);
//        }
//        return qualifier;
//    }
//
//    private byte[] crateRowKey(TSpan span, long acceptedTime) {
//        // distribute key evenly
//        byte[] applicationTraceIndexRowKey = SpanUtils.getApplicationTraceIndexRowKey(span.getApplicationName(), acceptedTime);
//        return rowKeyDistributor.getDistributedKey(applicationTraceIndexRowKey);
//    }
//}
