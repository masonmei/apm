package com.baidu.oped.apm.mvc.controller;

import com.baidu.oped.apm.model.service.TraceIndexService;
import com.baidu.oped.apm.model.service.TransactionService;
import com.baidu.oped.apm.mvc.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mason on 8/13/15.
 */
@RestController
@RequestMapping("transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TraceIndexService traceIndexService;

    @RequestMapping(value = {"top"})
    public List<TransactionSummary> top(@RequestParam("appName") String applicationName,
                                        @RequestParam(value = "number", required = false, defaultValue = "5")
                                        int number,
                                        @RequestParam(value = "instanceId", required = false) String instanceId,
                                        @RequestParam(value = "from", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
                                        @RequestParam(value = "to", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to) {
        List<TransactionSummary> summaries = list(applicationName, instanceId, from, to);
        return summaries.stream()
                .sorted((compare, with) -> Double.compare(compare.getRtMetric().getAvg(), with.getRtMetric().getAvg()))
                .limit(number).collect(Collectors.toList());
    }

    @RequestMapping(value = {"list"})
    public List<TransactionSummary> list(@RequestParam("appName") String applicationName,
                                         @RequestParam(value = "instanceId", required = false) String instanceId,
                                         @RequestParam(value = "from", required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
                                         @RequestParam(value = "to", required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to) {
        if (from == null ) {
            from = LocalDateTime.now().plus(Duration.ofMinutes(-5));
        }

        if (to == null || to.isBefore(from)){
            to = LocalDateTime.now();
        }

        Range range = new Range(from.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                                       to.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        List<TransactionId> transactionIds = traceIndexService.selectTraceIds(applicationName, range);

        BusinessTransactions transactions =
                transactionService.selectTransactions(transactionIds, applicationName, range, "rpc");

        Collection<BusinessTransaction> businessTransaction = transactions.getBusinessTransaction();
        List<TransactionSummary> summaryList =
                businessTransaction.stream().map(transaction -> new TransactionSummary(transaction, range))
                        .collect(Collectors.toList());

        return summaryList;
    }
}
