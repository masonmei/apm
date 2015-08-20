package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.BaseRepository;
import com.baidu.oped.apm.collector.dao.ApplicationTraceIndexDao;
import com.baidu.oped.apm.collector.util.AcceptedTimeService;
import com.baidu.oped.apm.common.entity.ApplicationTraceIndex;
import com.baidu.oped.apm.common.util.SpanUtils;
import com.baidu.oped.apm.common.util.TransactionId;
import com.baidu.oped.apm.common.util.TransactionIdUtils;
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

        ApplicationTraceIndex traceIndex = new ApplicationTraceIndex();
        traceIndex.setApplicationName(span.getApplicationName());
        traceIndex.setElapsed(span.getElapsed());
        traceIndex.setErr(span.getErr());
        traceIndex.setAcceptedTime(acceptedTimeService.getAcceptedTime());

        byte[] transactionIdBytes = span.getTransactionId();
        TransactionId transactionId = TransactionIdUtils.parseTransactionId(transactionIdBytes);
        String agentId = transactionId.getAgentId();
        if (agentId == null) {
            agentId = span.getAgentId();
        }

        traceIndex.setTransactionSequence(transactionId.getTransactionSequence());
        traceIndex.setAgentId(agentId);
        traceIndex.setAgentStartTime(transactionId.getAgentStartTime());

        save(traceIndex);
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
