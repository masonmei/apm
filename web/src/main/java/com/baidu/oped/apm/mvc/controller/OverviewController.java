package com.baidu.oped.apm.mvc.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.mvc.vo.Instance;
import com.baidu.oped.apm.mvc.vo.Transaction;
import com.baidu.oped.apm.mvc.vo.TrendResponse;

/**
 * Created by mason on 8/25/15.
 */
@RestController
@RequestMapping("overview/applications/${appId}")
public class OverviewController {

    /**
     * Get Application Response Time trend data
     *
     * @param appId
     * @param from
     * @param to
     * @param period
     */
    @RequestMapping(value = {"trend/rt"}, method = RequestMethod.GET)
    public TrendResponse responseTime(
                @PathVariable("appId") Long appId,
                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
                @RequestParam(value = "to", required = false)
                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
                @RequestParam("period") Integer period) {

        return null;
    }

    /**
     * Get Application Apdex trend data.
     *
     * @param appId
     * @param from
     * @param to
     * @param period
     * @return
     */
    @RequestMapping(value = {"trend/apdex"}, method = RequestMethod.GET)
    public TrendResponse apdex(
                @PathVariable("appId") Long appId,
                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
                @RequestParam(value = "to", required = false)
                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
                @RequestParam("period") Integer period) {

        return null;
    }

    /**
     * Get Application count per minute trend data.
     *
     * @param appId
     * @param from
     * @param to
     * @param period
     * @return
     */
    @RequestMapping(value = {"trend/cpm"}, method = RequestMethod.GET)
    public TrendResponse cpm(
               @PathVariable("appId") Long appId,
               @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
               @RequestParam(value = "to", required = false)
               @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
               @RequestParam("period") Integer period) {

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
                @PathVariable("appId") Long appId,
                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
                @RequestParam(value = "to", required = false)
                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
                @RequestParam("limit") Integer limit) {

        return null;
    }

    /**
     * Get Application error Rate trend data.
     *
     * @param appId
     * @param from
     * @param to
     * @param period
     * @return
     */
    @RequestMapping(value = {"trend/errorRate"}, method = RequestMethod.GET)
    public TrendResponse errorRate(
                @PathVariable("appId") Long appId,
                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
                @RequestParam(value = "to", required = false)
                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
                @RequestParam("period") Integer period) {

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
            @PathVariable("appId") Long appId,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to) {
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
            @PathVariable("appId") Long appId,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to) {

        return null;
    }

    /**
     * Get Instance Response Time trend data
     *
     * @param appId
     * @param from
     * @param to
     * @param period
     */
    @RequestMapping(value = {"instances/${instanceId}/trend/rt"}, method = RequestMethod.GET)
    public TrendResponse instnceResponseTime(
            @PathVariable("appId") Long appId,
            @PathVariable("instanceId") Long instanceId,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam("period") Integer period) {

        return null;
    }

    /**
     * Get Instance Apdex trend data.
     *
     * @param appId
     * @param from
     * @param to
     * @param period
     * @return
     */
    @RequestMapping(value = {"instances/${instanceId}/trend/apdex"}, method = RequestMethod.GET)
    public TrendResponse instanceApdex(
            @PathVariable("appId") Long appId,
            @PathVariable("instanceId") Long instanceId,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam("period") Integer period) {

        return null;
    }

    /**
     * Get Instance count per minute trend data.
     *
     * @param appId
     * @param from
     * @param to
     * @param period
     * @return
     */
    @RequestMapping(value = {"instances/${instanceId}/trend/cpm"}, method = RequestMethod.GET)
    public TrendResponse instanceCpm(
            @PathVariable("appId") Long appId,
            @PathVariable("instanceId") Long instanceId,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam("period") Integer period) {

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
    @RequestMapping(value = {"instances/${instanceId}/transactions"}, method = RequestMethod.GET)
    public List<Transaction> instanceTransaction(
            @PathVariable("appId") Long appId,
            @PathVariable("instanceId") Long instanceId,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam("limit") Integer limit) {

        return null;
    }

    /**
     * Get Application error Rate trend data.
     *
     * @param appId
     * @param from
     * @param to
     * @param period
     * @return
     */
    @RequestMapping(value = {"instances/${instanceId}/trend/errorRate"}, method = RequestMethod.GET)
    public TrendResponse instanceErrorRate(
            @PathVariable("appId") Long appId,
            @PathVariable("instanceId") Long instanceId,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to,
            @RequestParam("period") Integer period) {

        return null;
    }

    /**
     * Get Application alarming information.
     *
     * @param appId
     * @param from
     * @param to
     */
    @RequestMapping(value = {"instances/${instanceId}/alarm"}, method = RequestMethod.GET)
    public void instanceAlarm(
            @PathVariable("appId") Long appId,
            @PathVariable("instanceId") Long instanceId,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to) {
        throw new UnsupportedOperationException("not supported yet!");
    }

}
