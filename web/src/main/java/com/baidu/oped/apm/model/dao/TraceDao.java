//
//package com.baidu.oped.apm.model.dao;
//
//
//import java.util.Collection;
//import java.util.List;
//
//import com.baidu.oped.apm.common.bo.SpanBo;
//import com.baidu.oped.apm.mvc.vo.TransactionId;
//
///**
// * class TraceDao
// *
// * @author meidongxu@baidu.com
// */
//public interface TraceDao {
//
//    List<SpanBo> selectSpan(TransactionId transactionId);
//
//    List<SpanBo> selectSpanAndAnnotation(TransactionId transactionId);
//
//    List<List<SpanBo>> selectSpans(List<TransactionId> transactionIdList);
//
//    List<List<SpanBo>> selectAllSpans(Collection<TransactionId> transactionIdList);
//
//    List<SpanBo> selectSpans(TransactionId transactionId);
//
//}
