//
//package com.baidu.oped.apm.collector.dao.hbase;
//
//import com.baidu.oped.apm.collector.dao.MapResponseTimeDao;
//import com.baidu.oped.apm.collector.dao.hbase.statistics.*;
//import com.baidu.oped.apm.collector.util.AcceptedTimeService;
//import com.baidu.oped.apm.collector.util.ConcurrentCounterMap;
//import com.baidu.oped.apm.common.ServiceType;
//import com.baidu.oped.apm.common.hbase.HbaseOperations2;
//import com.baidu.oped.apm.common.util.ApplicationMapStatisticsUtils;
//import com.baidu.oped.apm.common.util.TimeSlot;
//
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
//import static com.baidu.oped.apm.common.hbase.HBaseTables.*;
//
///**
// * Save response time data of WAS
// *
// * @author netspider
// * @author emeroad
// */
//@Repository
//public class HbaseMapResponseTimeDao implements MapResponseTimeDao {
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
//    @Qualifier("selfMerge")
//    private RowKeyMerge rowKeyMerge;
//
//    private final boolean useBulk;
//
//    private final ConcurrentCounterMap<RowInfo> counter = new ConcurrentCounterMap<RowInfo>();
//
//    public HbaseMapResponseTimeDao() {
//        this(true);
//    }
//
//    public HbaseMapResponseTimeDao(boolean useBulk) {
//        this.useBulk = useBulk;
//    }
//
//    @Override
//    public void received(String applicationName, short applicationServiceType, String agentId, int elapsed, boolean isError) {
//        if (applicationName == null) {
//            throw new NullPointerException("applicationName must not be null");
//        }
//        if (agentId == null) {
//            throw new NullPointerException("agentId must not be null");
//        }
//
//        if (logger.isDebugEnabled()) {
//            logger.debug("[Received] {} ({})[{}]",
//                    applicationName, ServiceType.findServiceType(applicationServiceType), agentId);
//        }
//
//
//        // make row key. rowkey is me
//        final long acceptedTime = acceptedTimeService.getAcceptedTime();
//        final long rowTimeSlot = timeSlot.getTimeSlot(acceptedTime);
//        final RowKey selfRowKey = new CallRowKey(applicationName, applicationServiceType, rowTimeSlot);
//
//        final short slotNumber = ApplicationMapStatisticsUtils.getSlotNumber(applicationServiceType, elapsed, isError);
//        final ColumnName selfColumnName = new ResponseColumnName(agentId, slotNumber);
//        if (useBulk) {
//            RowInfo rowInfo = new DefaultRowInfo(selfRowKey, selfColumnName);
//            this.counter.increment(rowInfo, 1L);
//        } else {
//            final byte[] rowKey = selfRowKey.getRowKey();
//            // column name is the name of caller app.
//            byte[] columnName = selfColumnName.getColumnName();
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
//        hbaseTemplate.incrementColumnValue(MAP_STATISTICS_SELF, rowKey, MAP_STATISTICS_SELF_CF_COUNTER, columnName, increment);
//    }
//
//
//    @Override
//    public void flushAll() {
//        if (!useBulk) {
//            throw new IllegalStateException("useBulk is " + useBulk);
//        }
//
//        // update statistics by rowkey and column for now. need to update it by rowkey later.
//        Map<RowInfo,ConcurrentCounterMap.LongAdder> remove = this.counter.remove();
//        List<Increment> merge = rowKeyMerge.createBulkIncrement(remove);
//        if (!merge.isEmpty()) {
//            if (logger.isDebugEnabled()) {
//                logger.debug("flush {} Increment:{}", this.getClass().getSimpleName(), merge.size());
//            }
//            hbaseTemplate.increment(MAP_STATISTICS_SELF, merge);
//        }
//
//    }
//}
