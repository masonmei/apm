package com.baidu.oped.apm.mvc.controller;

import com.baidu.oped.apm.common.Exception.ParameterException;
import com.baidu.oped.apm.common.util.TimeUtils;
import com.baidu.oped.apm.model.service.TraceIndexService;
import com.baidu.oped.apm.model.service.TransactionService;
import com.baidu.oped.apm.mvc.vo.BusinessTransactions;
import com.baidu.oped.apm.mvc.vo.DataTrend;
import com.baidu.oped.apm.mvc.vo.Range;
import com.baidu.oped.apm.mvc.vo.TransactionId;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mason on 8/13/15.
 */
@RestController
@RequestMapping("trend")
public class TrendController {

    @Autowired
    TraceIndexService traceIndexService;

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = {"overview"})
    public DataTrend overview(
            @RequestParam(value = "appName", required = true) String applicationName,
            @RequestParam(value = "agentId", required = false, defaultValue = "") String agentId,
            @RequestParam(value = "startTime", required = true) String startTime,
            @RequestParam(value = "endTime", required = true) String endTime,
            @RequestParam(value = "period", required = true) int period,
            @RequestParam(value = "contrast", required = false, defaultValue = "false") boolean contrast
    ) throws Exception {
        System.out.println("startTime:" + startTime + ", endTime:" + endTime);
        long start, end;
        try {
            if (period < 60) {
                throw new ParameterException("period must more than 60");
            }
            period = period / 60 * 60; // 规整period
            startTime = "2015-08-14 10:00:00";
            endTime = "2015-08-14 11:00:00";
            start = TimeUtils.getTimeStampL(startTime);
            end = TimeUtils.getTimeStampL(endTime);
            start = 1439517600000L;
            end = 1439521200000L;
        }catch (Exception e) {
            throw new ParameterException(e);
        }

        System.out.println("start:" + start + ", end:" + end + ", appName:" + applicationName + ", period:" + period);

        Range range = new Range(start, end);
        List<TransactionId> transactionIds = traceIndexService.selectTraceIds(applicationName, range);

        List<TransactionId> neededTransactionIds = new ArrayList<TransactionId>();
        if (StringUtils.isNotEmpty(agentId)) {
            for (TransactionId transactionId : transactionIds) {
                if (transactionId.getAgentId().equalsIgnoreCase(agentId)) {
                    neededTransactionIds.add(transactionId);
                }
            }
        } else {
            neededTransactionIds = transactionIds;
        }
        System.out.println("transactionIds:" + transactionIds);
        BusinessTransactions transactions =
                transactionService.selectTransactions(neededTransactionIds, applicationName, range,
                        "serviceType");

        System.out.println("BusinessTransactions:" + transactions);

        return transactionService.packageDataTrend(transactions, period);
    }

    @RequestMapping(value = {"apdex"})
    public void apdex() {

    }

    @RequestMapping(value = {"error"})
    public void error() {

    }
}
