package com.baidu.oped.apm.mvc.controller;

import com.baidu.oped.apm.common.Exception.ParameterException;
import com.baidu.oped.apm.model.service.TransactionService;
import com.baidu.oped.apm.mvc.vo.BusinessTransactions;
import com.baidu.oped.apm.mvc.vo.DataTrend;
import com.baidu.oped.apm.mvc.vo.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by mason on 8/13/15.
 */
@RestController
@RequestMapping("api/apm/trend")
public class TrendController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = {"overview"})
    public DataTrend overview(
            @RequestParam(value = "appName", required = true) String applicationName,
            @RequestParam(value = "agentId", required = false, defaultValue = "") String agentId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam(value = "period", required = true) int period,
            @RequestParam(value = "contrast", required = false, defaultValue = "false") boolean contrast
    ) throws Exception {
        if (period < 60) {
            throw new ParameterException("period must more than 60");
        }

        if (from == null) {
            from = LocalDateTime.now().plus(Duration.ofMinutes(-5));
        }

        if (to == null || to.isBefore(from)) {
            to = LocalDateTime.now();
        }

        Range range = new Range(from.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                to.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        BusinessTransactions transactions =
                transactionService.selectTransactions(applicationName, agentId, range,
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
