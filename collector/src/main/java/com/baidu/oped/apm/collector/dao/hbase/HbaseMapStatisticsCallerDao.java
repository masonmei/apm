//
//package com.baidu.oped.apm.collector.dao.hbase;
//
//import static com.baidu.oped.apm.common.hbase.HBaseTables.*;
//
//import com.baidu.oped.apm.collector.dao.MapStatisticsCallerDao;
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
// * Update statistics of caller node
// *
// * @author netspider
// * @author emeroad
// */
//@Repository
//public class HbaseMapStatisticsCallerDao implements MapStatisticsCallerDao {
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
//    @Qualifier("callerMerge")
//    private RowKeyMerge rowKeyMerge;
//
//    @Autowired
//    private TimeSlot timeSlot;
//
//    private final boolean useBulk;
//
//    private final ConcurrentCounterMap<RowInfo> counter = new ConcurrentCounterMap<RowInfo>();
//
//    public HbaseMapStatisticsCallerDao() {
//        this(true);
//    }
//
//    public HbaseMapStatisticsCallerDao(boolean useBulk) {
//        this.useBulk = useBulk;
//    }
//
//    @Override
//    public void update(String callerApplicationName, short callerServiceType, String callerAgentid, String calleeApplicationName, short calleeServiceType, String calleeHost, int elapsed, boolean isError) {
//        if (callerApplicationName == null) {
//            throw new NullPointerException("callerApplicationName must not be null");
//        }
//        if (calleeApplicationName == null) {
//            throw new NullPointerException("calleeApplicationName must not be null");
//        }
//
//        if (logger.isDebugEnabled()) {
//            logger.debug("[Caller] {} ({}) {} -> {} ({})[{}]",
//                    callerApplicationName, ServiceType.findServiceType(callerServiceType), callerAgentid,
//                    calleeApplicationName, ServiceType.findServiceType(calleeServiceType), calleeHost);
//        }
//
//        // there may be no endpoint in case of httpclient
//        calleeHost = StringUtils.defaultString(calleeHost);
//
//        // make row key. rowkey is me
//        final long acceptedTime = acceptedTimeService.getAcceptedTime();
//        final long rowTimeSlot = timeSlot.getTimeSlot(acceptedTime);
//        final RowKey callerRowKey = new CallRowKey(callerApplicationName, callerServiceType, rowTimeSlot);
//
//        final short calleeSlotNumber = ApplicationMapStatisticsUtils.getSlotNumber(calleeServiceType, elapsed, isError);
//        final ColumnName calleeColumnName = new CalleeColumnName(callerAgentid, calleeServiceType, calleeApplicationName, calleeHost, calleeSlotNumber);
//        if (useBulk) {
//            RowInfo rowInfo = new DefaultRowInfo(callerRowKey, calleeColumnName);
//            this.counter.increment(rowInfo, 1L);
//        } else {
//            final byte[] rowKey = callerRowKey.getRowKey();
//            // column name is the name of caller app.
//            byte[] columnName = calleeColumnName.getColumnName();
//            increment(rowKey, columnName, 1L);
//        }
//    }
//
//    private void increment(byte[] rowKey, byte[] columnName, long increment) {
//        if (rowKey == null) {
//            throw new NullPointerException("rowKey must not be null");
//        }
//        if (columnName == null) {
//            throw new NullPointerException("columnName must not be null");
//        }
//        hbaseTemplate.incrementColumnValue(MAP_STATISTICS_CALLEE, rowKey, MAP_STATISTICS_CALLEE_CF_VER2_COUNTER, columnName, increment);
//    }
//
//
//    @Override
//    public void flushAll() {
//        if (!useBulk) {
//            throw new IllegalStateException();
//        }
//        // update statistics by rowkey and column for now. need to update it by rowkey later.
//        Map<RowInfo,ConcurrentCounterMap.LongAdder> remove = this.counter.remove();
//        List<Increment> merge = rowKeyMerge.createBulkIncrement(remove);
//        if (!merge.isEmpty()) {
//            if (logger.isDebugEnabled()) {
//                logger.debug("flush {} Increment:{}", this.getClass().getSimpleName(), merge.size());
//            }
//            hbaseTemplate.increment(MAP_STATISTICS_CALLEE, merge);
//        }
//
//    }
//}
