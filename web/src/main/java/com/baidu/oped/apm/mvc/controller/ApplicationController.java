package com.baidu.oped.apm.mvc.controller;

import com.baidu.oped.apm.model.service.ApplicationService;
import com.baidu.oped.apm.mvc.vo.Application;
import com.baidu.oped.apm.mvc.vo.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to) {

        return Collections.emptyList();
        //        return applicationService.selectAllApplicationNames();
    }

    /**
     * 根据application名字获取对应的实例列表
     *
     * @param applicationName
     * @param simplify
     * @param from
     * @param to
     * @return
     */
    @RequestMapping(value = {"${appName}/instances"}, method = RequestMethod.GET)
    public List<Instance> findApplicationInstance(
             @PathVariable("appName") String applicationName,
             @RequestParam(value = "simplify", required = false) boolean simplify,
             @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
             @RequestParam(value = "to", required = false)
             @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to) {
        return applicationService.findApplicationInstanceByApplication(applicationName, simplify);
    }


}
