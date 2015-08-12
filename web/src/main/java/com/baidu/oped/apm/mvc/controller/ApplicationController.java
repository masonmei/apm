package com.baidu.oped.apm.mvc.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.model.service.ApplicationService;
import com.baidu.oped.apm.mvc.vo.Application;
import com.baidu.oped.apm.mvc.vo.Instance;

/**
 * Created by mason on 8/12/15.
 */
@RestController
@RequestMapping("application")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(value = {"list"})
    public List<Application> findApplication() {
        return Collections.emptyList();
    }

    @RequestMapping(value = {"instance/list"})
    public List<Instance> findApplicationInstance(@RequestParam("appName") String applicationName) {
        return applicationService.findApplicationInstanceByApplication(applicationName);
    }
}
