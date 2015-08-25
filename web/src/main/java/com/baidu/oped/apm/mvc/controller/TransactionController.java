package com.baidu.oped.apm.mvc.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.model.service.TransactionService;
import com.baidu.oped.apm.mvc.vo.BusinessTransaction;
import com.baidu.oped.apm.mvc.vo.BusinessTransactions;
import com.baidu.oped.apm.mvc.vo.Range;
import com.baidu.oped.apm.mvc.vo.Transaction;
import com.baidu.oped.apm.mvc.vo.TransactionSummary;
import com.baidu.oped.apm.mvc.vo.TrendResponse;

/**
 * Created by mason on 8/13/15.
 */
@RestController
@RequestMapping("transactions/applications/${appId}")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /**
     * List Given application transactions.
     *
     * @param appId
     * @param from
     * @param to
     * @param limit
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Transaction> transactions(
            @PathVariable("appId") Long appId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam("limit") Integer limit){

        return null;
    }

    /**
     * Get top n transaction response time trend data.
     *
     * @param appId
     * @param from
     * @param to
     * @param period
     * @param limit
     * @return
     */
    @RequestMapping(value = {"trend/rt"})
    public TrendResponse responseTime(
            @PathVariable("appId") Long appId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam("period") Integer period,
            @RequestParam("limit") Integer limit) {

        return null;
    }

    /**
     *  Get all transaction cpm of the application.
     *
     * @param appId
     * @param from
     * @param to
     * @param period
     */
    @RequestMapping(value = {"trend/cpm"})
    public TrendResponse cpm(
            @PathVariable("appId") Long appId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam("period") Integer period) {

        return null;
    }

    /**
     * Get all transactions order by response time desc.
     *
     * @param appId
     * @param from
     * @param to
     * @param pageCount
     * @param pageSize
     * @param orderby
     * @return
     */
    @RequestMapping(value = {"traces"})
    public List<Transaction> slowTransactions(
            @PathVariable("appId") Long appId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam(value = "pageCount", defaultValue = "1") Integer pageCount,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderby", required = false) String orderby) {

        return null;
    }

    /**
     * List transactions of given instance.
     *
     * @param appId
     * @param instanceId
     * @param from
     * @param to
     * @param limit
     * @return
     */
    @RequestMapping(value = {"instances/${instanceId}"}, method = RequestMethod.GET)
    public List<Transaction> instanceTransactions(
            @PathVariable("appId") Long appId,
            @PathVariable("instanceId") Long instanceId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam("limit") Integer limit){

        return null;
    }

    /**
     * Get top n transaction response time trend data of given instance.
     *
     * @param appId
     * @param instanceId
     * @param from
     * @param to
     * @param period
     * @param limit
     * @return
     */
    @RequestMapping(value = {"instances/${instanceId}/trend/rt"})
    public TrendResponse instanceResponseTime(
            @PathVariable("appId") Long appId,
            @PathVariable("instanceId") Long instanceId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam("period") Integer period,
            @RequestParam("limit") Integer limit) {

        return null;
    }

    /**
     * Get all transaction cpm of given instance.
     *
     * @param appId
     * @param instanceId
     * @param from
     * @param to
     * @param period
     * @return
     */
    @RequestMapping(value = {"instances/${instanceId}/trend/cpm"})
    public TrendResponse instanceCpm(
            @PathVariable("appId") Long appId,
            @PathVariable("instanceId") Long instanceId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam("period") Integer period) {

        return null;
    }

    /**
     * Get all transactions order by response time desc.
     *
     * @param appId
     * @param from
     * @param to
     * @param pageCount
     * @param pageSize
     * @param orderby
     * @return
     */
    @RequestMapping(value = {"instances/${instanceId}/traces"})
    public List<Transaction> instanceSlowTransactions(
            @PathVariable("appId") Long appId,
            @PathVariable("instanceId") Long instanceId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam(value = "pageCount", defaultValue = "1") Integer pageCount,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderby", required = false) String orderby) {

        return null;
    }


//
//    @RequestMapping(value = {"${transactionId}/trend/execute"})
//    public void test5(@PathVariable String transactionId) {
//        return;
//    }
//
//    @RequestMapping(value = {"${transactionId}/trend/cpm"})
//    public void test6(@PathVariable String transactionId) {
//        return;
//    }
//
//    @RequestMapping(value = {"${transactionId}/breakdown"})
//    public void test7(@PathVariable String transactionId) {
//        return;
//    }
//
//    @RequestMapping(value = {"${transactionId}/slow"})
//    public void test8(@PathVariable String transactionId) {
//        return;
//    }
//
//    @RequestMapping(value = {"${transactionId}/details"})
//    public void test9(@PathVariable String transactionId) {
//        return;
//    }
//
//    @RequestMapping(value = {"${transactionId}/details/trend/rt"})
//    public void test10(@PathVariable String transactionId) {
//        return;
//    }
//
//    @RequestMapping(value = {"${transactionId}/details/trend/error"})
//    public void test11(@PathVariable String transactionId) {
//        return;
//    }
//
//    @RequestMapping(value = {"${transactionId}/details/trend/apdex"})
//    public void test12() {
//        return;
//    }
//
//    @RequestMapping(value = {"${transactionId}/details/trend/slow"})
//    public void test13() {
//        return;
//    }
}
