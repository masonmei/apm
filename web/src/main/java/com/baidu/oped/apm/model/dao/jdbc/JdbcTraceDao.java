package com.baidu.oped.apm.model.dao.jdbc;

import java.util.Collection;
import java.util.List;

import com.baidu.oped.apm.common.bo.SpanBo;
import com.baidu.oped.apm.model.dao.TraceDao;
import com.baidu.oped.apm.mvc.vo.TransactionId;

/**
 * Created by mason on 8/16/15.
 */
public class JdbcTraceDao implements TraceDao {
    @Override
    public List<SpanBo> selectSpan(TransactionId transactionId) {
        return null;
    }

    @Override
    public List<SpanBo> selectSpanAndAnnotation(TransactionId transactionId) {
        return null;
    }

    @Override
    public List<List<SpanBo>> selectSpans(List<TransactionId> transactionIdList) {
        return null;
    }

    @Override
    public List<List<SpanBo>> selectAllSpans(Collection<TransactionId> transactionIdList) {
        return null;
    }

    @Override
    public List<SpanBo> selectSpans(TransactionId transactionId) {
        return null;
    }
}
