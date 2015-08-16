//
//package com.baidu.oped.apm.collector.dao.hbase;
//
//import static com.baidu.oped.apm.common.hbase.HBaseTables.*;
//
//import com.baidu.oped.apm.collector.dao.MapStatisticsCalleeDao;
//import com.baidu.oped.apm.collector.dao.hbase.statistics.*;
//import com.baidu.oped.apm.collector.util.AcceptedTimeService;
//import com.baidu.oped.apm.collector.util.ConcurrentCounterMap;
//import com.baidu.oped.apm.common.ServiceType;
//import com.baidu.oped.apm.common.hbase.HbaseOperations2;
//import com.baidu.oped.apm.common.util.ApplicationMapStatisticsUtils;
//import com.baidu.oped.apm.common.util.TimeSlot;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.hadoop.hbase.client.Increment;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Update statistics of callee node
// *
// * @author netspider
// * @author emeroad
// */
//@Repository
//public class HbaseMapStatisticsCalleeDao implements MapStatisticsCalleeDao {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private HbaseOperations2 hbaseTemplate;
//
//    @Autowired
//    private AcceptedTimeService acceptedTimeService;
//
//    @Autowired
//    private TimeSlot timeSlot;
//
//    @Autowired
//    @Qualifier("calleeMerge")
//    private RowKeyMerge rowKeyMerge;
//
//    private final boolean useBulk;
//
//    private final ConcurrentCounterMap<RowInfo> counter = new ConcurrentCounterMap<RowInfo>();
//
//    public HbaseMapStatisticsCalleeDao() {
//        this(true);
//    }
//
//    public HbaseMapStatisticsCalleeDao(boolean useBulk) {
//        this.useBulk = useBulk;
//    }
//
//
//    @Override
//    public void update(String calleeApplicationName, short calleeServiceType, String callerApplicationName, short callerServiceType, String callerHost, int elapsed, boolean isError) {
//        if (callerApplicationName == null) {
//            throw new NullPointerException("callerApplicationName must not be null");
//        }
//        if (calleeApplicationName == null) {
//            throw new NullPointerException("calleeApplicationName must not be null");
//        }
//
//        if (logger.isDebugEnabled()) {
//            logger.debug("[Callee] {} ({}) <- {} ({})[{}]",
//                    calleeApplicationName, ServiceType.findServiceType(calleeServiceType),
//                    callerApplicationName, ServiceType.findServiceType(callerServiceType), callerHost);
//        }
//
//        // there may be no endpoint in case of httpclient
//        callerHost = StringUtils.defaultString(callerHost);
//
//
//        // make row key. rowkey is me
//        final long acceptedTime = acceptedTimeService.getAcceptedTime();
//        final long rowTimeSlot = timeSlot.getTimeSlot(acceptedTime);
//        final RowKey calleeRowKey = new CallRowKey(calleeApplicationName, calleeServiceType, rowTimeSlot);
//
//        final short callerSlotNumber = ApplicationMapStatisticsUtils.getSlotNumber(callerServiceType, elapsed, isError);
//        final ColumnName callerColumnName = new CallerColumnName(callerServiceType, callerApplicationName, callerHost, callerSlotNumber);
//
//        if (useBulk) {
//            RowInfo rowInfo = new DefaultRowInfo(calleeRowKey, callerColumnName);
//            counter.increment(rowInfo, 1L);
//        } else {
//            final byte[] rowKey = calleeRowKey.getRowKey();
//
//            // column name is the name of caller app.
//            byte[] columnName = callerColumnName.getColumnName();
//            increment(rowKey, columnName, 1L);
//        }
//    }
//
//
//
//    private void increment(byte[] rowKey, byte[] columnName, long increment) {
//        if (rowKey == null) {
//            throw new NullPointerException("rowKey must not be null");
//        }
//        if (columnName == null) {
//            throw new NullPointerException("columnName must not be null");
//        }
//        hbaseTemplate.incrementColumnValue(MAP_STATISTICS_CALLER, rowKey, MAP_STATISTICS_CALLER_CF_COUNTER, columnName, increment);
//    }
//
//    @Override
//    public void flushAll() {
//        if (!useBulk) {
//            throw new IllegalStateException();
//        }
//
//        Map<RowInfo, ConcurrentCounterMap.LongAdder> remove = this.counter.remove();
//        List<Increment> merge = rowKeyMerge.createBulkIncrement(remove);
//        if (!merge.isEmpty()) {
//            if (logger.isDebugEnabled()) {
//                logger.debug("flush {} Increment:{}", this.getClass().getSimpleName(), merge.size());
//            }
//            hbaseTemplate.increment(MAP_STATISTICS_CALLER, merge);
//        }
//
//    }
//}
