package com.baidu.oped.apm.mvc.controller;

import com.baidu.oped.apm.model.service.ApplicationService;
import com.baidu.oped.apm.mvc.vo.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by mason on 8/12/15.
 */
@RestController
@RequestMapping("api/apm")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    /**
     * 获取所有app列表
     * @return
     */
    @RequestMapping(value = {"list"})
    public List<String> findApplication() {
        return applicationService.selectAllApplicationNames();
    }

    /**
     * 根据application名字获取对应的实例列表
     * @param applicationName
     * @param simplify
     * @return
     */
    @RequestMapping(value = {"instance/list"})
    public List<Instance> findApplicationInstance(
             @RequestParam("appName") String applicationName,
             @RequestParam(value = "simplify", required = false) boolean simplify) {
        return applicationService.findApplicationInstanceByApplication(applicationName, simplify);
    }


}
