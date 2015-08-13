package com.baidu.oped.apm.mvc.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.mvc.vo.DataPoint;
import com.baidu.oped.apm.mvc.vo.TimeRegion;

/**
 * Created by mason on 8/13/15.
 */
@RestController
@RequestMapping("trend")
public class TrendController {

    @RequestMapping(value = {"overview"})
    public Map<String, List<DataPoint>> overview(
            @RequestParam("appName")String applicationName,
            @RequestParam(value = "instanceId", required = false) String instanceId,
            @RequestParam(value = "timeRegion") TimeRegion timeRegion,
            @RequestParam(value = "period") int period,
            @RequestParam(value = "contrast", required = false) boolean contrast
    ) {
        return Collections.emptyMap();
    }

    @RequestMapping(value = {"apdex"})
    public void apdex() {

    }

    @RequestMapping(value = {"error"})
    public void error() {

    }
}
