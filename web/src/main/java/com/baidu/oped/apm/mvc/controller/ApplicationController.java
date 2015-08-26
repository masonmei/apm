package com.baidu.oped.apm.mvc.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.model.service.ApplicationService;
import com.baidu.oped.apm.mvc.vo.Application;
import com.baidu.oped.apm.mvc.vo.Instance;
import com.baidu.oped.apm.utils.Constaints;

/**
 * Created by mason on 8/12/15.
 */
@RestController
@RequestMapping("applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /**
     * 获取所有app列表
     *
     * @param from
     * @param to
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Application> findApplication(
            @RequestParam(value = "simplify", required = false) boolean simplify,
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to) {

        return Collections.emptyList();
        //        return applicationService.selectAllApplicationNames();
    }

    /**
     * 根据application名字获取对应的实例列表
     *
     * @param applicationId
     * @param simplify
     * @param from
     * @param to
     * @return
     */
    @RequestMapping(value = {"instances"}, method = RequestMethod.GET)
    public List<Instance> findApplicationInstance(
             @RequestParam("appId") String applicationId,
             @RequestParam(value = "simplify", required = false) boolean simplify,
             @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
             @RequestParam(value = "to", required = false)
             @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to) {
        return applicationService.findApplicationInstanceByApplication(applicationId, simplify);
    }


}
