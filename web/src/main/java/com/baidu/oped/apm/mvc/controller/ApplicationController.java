package com.baidu.oped.apm.mvc.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.common.jpa.entity.Application;
import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.Instance;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.model.service.ApplicationService;
import com.baidu.oped.apm.mvc.vo.ApplicationVo;
import com.baidu.oped.apm.mvc.vo.InstanceVo;
import com.baidu.oped.apm.mvc.vo.QueryResponse;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.utils.Constaints;
import com.baidu.oped.apm.utils.QueryUtils;
import com.baidu.oped.apm.utils.TimeUtils;

/**
 * Created by mason on 8/12/15.
 */
@RestController
@RequestMapping("applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /**
     * 获取 app 列表
     *
     * @param from
     * @param to
     * @param orderBy
     * @param pageSize
     * @param pageNumber
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public QueryResponse<ApplicationVo> applications(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN)
            LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN)
            LocalDateTime to,
            @RequestParam(value = "orderBy", required = false, defaultValue = Constaints.ORDER_DESC_ID)
            String orderBy,
            @RequestParam(value = "pageSize", required = false, defaultValue = Constaints.PAGE_SIZE)
            int pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = Constaints.PAGE_NUMBER)
            int pageNumber) {

        final long period = 60l;
        TimeRange timeRange = TimeUtils.createTimeRange(from, to);
        Page<Application> apps =
                applicationService.selectApplications(Constaints.DEFAULT_USER, orderBy, pageSize, pageNumber);

        Iterable<ApplicationStatistic> appStatistics =
                applicationService.selectApplicationStatistics(timeRange, apps, period);



        return QueryUtils.toApplicationResponse(apps, appStatistics, timeRange);
    }

    /**
     * 获取app对应的instance列表
     *
     * @param appId
     * @param from
     * @param to
     * @param orderBy
     * @param pageSize
     * @param pageNumber
     *
     * @return
     */
    @RequestMapping(value = {"instances"}, method = RequestMethod.GET)
    public QueryResponse<InstanceVo> findApplicationInstance(
            @RequestParam("appId")
            long appId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN)
            LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN)
            LocalDateTime to,
            @RequestParam(value = "orderBy", required = false, defaultValue = Constaints.ORDER_DESC_ID)
            String orderBy,
            @RequestParam(value = "pageSize", required = false, defaultValue = Constaints.PAGE_SIZE)
            int pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = Constaints.PAGE_NUMBER)
            int pageNumber) {
        final Long period = 60l;

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);
        Page<Instance> instances = applicationService.selectInstances(appId, orderBy, pageSize, pageNumber);
        Iterable<InstanceStatistic> instanceStatistics =
                applicationService.selectInstanceStatistics(timeRange, instances.getContent(), period);
        return QueryUtils.toInstanceResponse(appId, instances, instanceStatistics, timeRange);
    }

}
