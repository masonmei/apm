package com.baidu.oped.apm.mvc.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.mvc.vo.Instance;
import com.baidu.oped.apm.mvc.vo.Transaction;
import com.baidu.oped.apm.mvc.vo.TrendResponse;
import com.baidu.oped.apm.utils.Constaints;

/**
 * Created by mason on 8/25/15.
 */
@RestController
@RequestMapping("overview/applications")
public class OverviewController {

    /**
     * Get Application Response Time trend data
     *
     * @param appId
     * @param time
     * @param period
     */
    @RequestMapping(value = {"trend/rt"}, method = RequestMethod.GET)
    public TrendResponse responseTime(
                @RequestParam(value = "appId") Long appId,
                @RequestParam(value = "time") String[] time,
                @RequestParam(value = "period") Integer period) {

        return null;
    }

    /**
     * Get Application Apdex trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"trend/apdex"}, method = RequestMethod.GET)
    public TrendResponse apdex(
                @RequestParam(value = "appId") Long appId,
                @RequestParam(value = "time") String[] time,
                @RequestParam(value = "period") Integer period) {

        return null;
    }

    /**
     * Get Application count per minute trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"trend/cpm"}, method = RequestMethod.GET)
    public TrendResponse cpm(
               @RequestParam(value = "appId") Long appId,
               @RequestParam(value = "time") String[] time,
               @RequestParam(value = "period") Integer period) {

        return null;
    }

    /**
     * Get Application Transaction limit top n, ordered by average response time.
     *
     * @param appId
     * @param from
     * @param to
     * @param limit
     * @return
     */
    @RequestMapping(value = {"transactions"}, method = RequestMethod.GET)
    public List<Transaction> transaction(
                @RequestParam(value = "appId") Long appId,
                @RequestParam(value = "from", required = false)
                @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
                @RequestParam(value = "to", required = false)
                @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to,
                @RequestParam(value = "limit") Integer limit) {

        return null;
    }

    /**
     * Get Application error Rate trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"trend/errorRate"}, method = RequestMethod.GET)
    public TrendResponse errorRate(
                @RequestParam(value = "appId") Long appId,
                @RequestParam(value = "time") String[] time,
                @RequestParam(value = "period") Integer period) {

        return null;
    }

    /**
     * Get Application alarming information.
     *
     * @param appId
     * @param from
     * @param to
     */
    @RequestMapping(value = {"alarm"}, method = RequestMethod.GET)
    public void alarm(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to) {
        throw new UnsupportedOperationException("not supported yet!");
    }

    /**
     * Get Application instances
     * @param appId
     * @param from
     * @param to
     * @return
     */
    @RequestMapping(value = {"instances"}, method = RequestMethod.GET)
    public List<Instance> listInstance(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to) {

        return null;
    }

    /**
     * Get Instance Response Time trend data
     *
     * @param appId
     * @param time
     * @param period
     */
    @RequestMapping(value = {"instances/trend/rt"}, method = RequestMethod.GET)
    public TrendResponse instnceResponseTime(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "time") String[] time,
            @RequestParam(value = "period") Integer period) {

        return null;
    }

    /**
     * Get Instance Apdex trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"instances/trend/apdex"}, method = RequestMethod.GET)
    public TrendResponse instanceApdex(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "time") String[] time,
            @RequestParam(value = "period") Integer period) {

        return null;
    }

    /**
     * Get Instance count per minute trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"instances/trend/cpm"}, method = RequestMethod.GET)
    public TrendResponse instanceCpm(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "time") String[] time,
            @RequestParam(value = "period") Integer period) {

        return null;
    }

    /**
     * Get Instance Transaction limit top n, ordered by average response time.
     *
     * @param appId
     * @param from
     * @param to
     * @param limit
     * @return
     */
    @RequestMapping(value = {"instances/transactions"}, method = RequestMethod.GET)
    public List<Transaction> instanceTransaction(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to,
            @RequestParam(value = "limit") Integer limit) {

        return null;
    }

    /**
     * Get Application error Rate trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"instances/trend/errorRate"}, method = RequestMethod.GET)
    public TrendResponse instanceErrorRate(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "time") String[] time,
            @RequestParam(value = "period") Integer period) {

        return null;
    }

    /**
     * Get Application alarming information.
     *
     * @param appId
     * @param from
     * @param to
     */
    @RequestMapping(value = {"instances/alarm"}, method = RequestMethod.GET)
    public void instanceAlarm(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to) {
        throw new UnsupportedOperationException("not supported yet!");
    }

}
