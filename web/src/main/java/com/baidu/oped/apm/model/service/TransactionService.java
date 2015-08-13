package com.baidu.oped.apm.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.bo.SpanBo;
import com.baidu.oped.apm.model.dao.TraceDao;
import com.baidu.oped.apm.mvc.vo.BusinessTransactions;
import com.baidu.oped.apm.mvc.vo.Range;
import com.baidu.oped.apm.mvc.vo.TransactionId;

/**
 * Created by mason on 8/13/15.
 */
@Service
public class TransactionService {
    @Autowired
    private TraceDao traceDao;

    public BusinessTransactions selectTransactions(List<TransactionId> traceIds, String applicationName, Range range) {
        Assert.notNull(traceIds, "TraceIds cannot be null while selecting transactions.");
        Assert.hasLength(applicationName, "ApplicationName cannot be null while selecting transactions.");
        Assert.notNull(range, "Range cannot be null while selecting transactions.");

        List<List<SpanBo>> selectedSpansList = traceDao.selectSpans(traceIds);

        BusinessTransactions transactions = new BusinessTransactions();
        selectedSpansList.stream()
                .flatMap(spanBos -> spanBos.stream())
                .filter(spanBo -> applicationName.equals(spanBo.getApplicationId()))
                .forEach(spanBo -> transactions.add(spanBo));
        return transactions;
    }
}
