//
//package com.baidu.oped.apm.collector.dao.hbase.statistics;
//
//import com.baidu.oped.apm.collector.util.ConcurrentCounterMap;
//
//import org.apache.hadoop.hbase.client.Increment;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//
///**
// * class RowKeyMerge
// *
// * @author meidongxu@baidu.com
// */
//public class RowKeyMerge {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private final byte[] family;
//
//    public RowKeyMerge(byte[] family) {
//        if (family == null) {
//            throw new NullPointerException("family must not be null");
//        }
//        this.family = Arrays.copyOf(family, family.length);
//    }
//
//    public  List<Increment> createBulkIncrement(Map<RowInfo, ConcurrentCounterMap.LongAdder> data) {
//        if (data.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        final Map<RowKey, List<ColumnName>> rowkeyMerge = rowKeyBaseMerge(data);
//
//        List<Increment> incrementList = new ArrayList<Increment>();
//        for (Map.Entry<RowKey, List<ColumnName>> rowKeyEntry : rowkeyMerge.entrySet()) {
//            Increment increment = createIncrement(rowKeyEntry);
//            incrementList.add(increment);
//        }
//        return incrementList;
//    }
//
//    private Increment createIncrement(Map.Entry<RowKey, List<ColumnName>> rowKeyEntry) {
//        RowKey rowKey = rowKeyEntry.getKey();
//        final Increment increment = new Increment(rowKey.getRowKey());
//        for(ColumnName columnName : rowKeyEntry.getValue()) {
//            increment.addColumn(family, columnName.getColumnName(), columnName.getCallCount());
//        }
//        logger.trace("create increment row:{}, column:{}", rowKey, rowKeyEntry.getValue());
//        return increment;
//    }
//
//    private Map<RowKey, List<ColumnName>> rowKeyBaseMerge(Map<RowInfo, ConcurrentCounterMap.LongAdder> data) {
//        final Map<RowKey, List<ColumnName>> merge =  new HashMap<RowKey, List<ColumnName>>();
//
//        for (Map.Entry<RowInfo, ConcurrentCounterMap.LongAdder> entry : data.entrySet()) {
//            final RowInfo rowInfo = entry.getKey();
//            // write callCount to columnName and throw away
//            long callCount = entry.getValue().get();
//            rowInfo.getColumnName().setCallCount(callCount);
//
//            RowKey rowKey = rowInfo.getRowKey();
//            List<ColumnName> oldList = merge.get(rowKey);
//            if (oldList == null) {
//                List<ColumnName> newList = new ArrayList<ColumnName>();
//                newList.add(rowInfo.getColumnName());
//                merge.put(rowKey, newList);
//            } else {
//                oldList.add(rowInfo.getColumnName());
//            }
//        }
//        return merge;
//    }
//}
