package com.baidu.oped.apm.collector.dao.jdbc;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.collector.dao.MapStatisticsCallerDao;
import com.baidu.oped.apm.collector.dao.hbase.statistics.CallRowKey;
import com.baidu.oped.apm.collector.dao.hbase.statistics.CalleeColumnName;
import com.baidu.oped.apm.collector.dao.hbase.statistics.ColumnName;
import com.baidu.oped.apm.collector.dao.hbase.statistics.DefaultRowInfo;
import com.baidu.oped.apm.collector.dao.hbase.statistics.RowInfo;
import com.baidu.oped.apm.collector.dao.hbase.statistics.RowKey;
import com.baidu.oped.apm.collector.util.AcceptedTimeService;
import com.baidu.oped.apm.collector.util.ConcurrentCounterMap;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.common.util.ApplicationMapStatisticsUtils;
import com.baidu.oped.apm.common.util.TimeSlot;

/**
 * Created by mason on 8/17/15.
 */
@Component
public class JdbcMapStatisticsCallerDao implements MapStatisticsCallerDao {
    public static final Logger LOG = LoggerFactory.getLogger(JdbcMapStatisticsCallerDao.class);
    private final boolean useBulk;
    private final ConcurrentCounterMap<RowInfo> counter = new ConcurrentCounterMap<RowInfo>();
    @Autowired
    private AcceptedTimeService acceptedTimeService;
    @Autowired
    private TimeSlot timeSlot;
//        @Autowired
//        @Qualifier("callerMerge")
//        private RowKeyMerge rowKeyMerge;

    public JdbcMapStatisticsCallerDao() {
        this(true);
    }

    public JdbcMapStatisticsCallerDao(boolean useBulk) {
        this.useBulk = useBulk;
    }

    @Override
    public void update(String callerApplicationName, short callerServiceType, String callerAgentId,
                       String calleeApplicationName, short calleeServiceType, String calleeHost, int elapsed,
                       boolean isError) {
        if (callerApplicationName == null) {
            throw new NullPointerException("callerApplicationName must not be null");
        }
        if (calleeApplicationName == null) {
            throw new NullPointerException("calleeApplicationName must not be null");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[Caller] {} ({}) {} -> {} ({})[{}]", callerApplicationName,
                             ServiceType.findServiceType(callerServiceType), callerAgentId, calleeApplicationName,
                             ServiceType.findServiceType(calleeServiceType), calleeHost);
        }

        // there may be no endpoint in case of httpclient
        calleeHost = StringUtils.defaultString(calleeHost);

        // make row key. rowkey is me
        final long acceptedTime = acceptedTimeService.getAcceptedTime();
        final long rowTimeSlot = timeSlot.getTimeSlot(acceptedTime);
        final RowKey callerRowKey = new CallRowKey(callerApplicationName, callerServiceType, rowTimeSlot);

        final short calleeSlotNumber = ApplicationMapStatisticsUtils.getSlotNumber(calleeServiceType, elapsed, isError);
        final ColumnName calleeColumnName =
                new CalleeColumnName(callerAgentId, calleeServiceType, calleeApplicationName, calleeHost,
                                            calleeSlotNumber);
        if (useBulk) {
            RowInfo rowInfo = new DefaultRowInfo(callerRowKey, calleeColumnName);
            this.counter.increment(rowInfo, 1L);
        } else {
            final byte[] rowKey = callerRowKey.getRowKey();
            // column name is the name of caller app.
            byte[] columnName = calleeColumnName.getColumnName();
            increment(rowKey, columnName, 1L);
        }

    }

    @Override
    public void flushAll() {
        if (!useBulk) {
            throw new IllegalStateException();
        }
        // update statistics by rowkey and column for now. need to update it by rowkey later.
//        Map<RowInfo, ConcurrentCounterMap.LongAdder> remove = this.counter.remove();
//        List<Increment> merge = rowKeyMerge.createBulkIncrement(remove);
//        if (!merge.isEmpty()) {
//            if (LOG.isDebugEnabled()) {
//                LOG.debug("flush {} Increment:{}", this.getClass().getSimpleName(), merge.size());
//            }
//            hbaseTemplate.increment(MAP_STATISTICS_CALLEE, merge);
//        }
    }

    private void increment(byte[] rowKey, byte[] columnName, long increment) {
        if (rowKey == null) {
            throw new NullPointerException("rowKey must not be null");
        }
        if (columnName == null) {
            throw new NullPointerException("columnName must not be null");
        }

//        hbaseTemplate
//                .incrementColumnValue(MAP_STATISTICS_CALLEE, rowKey, MAP_STATISTICS_CALLEE_CF_VER2_COUNTER, columnName,
//                                             increment);
    }

}


