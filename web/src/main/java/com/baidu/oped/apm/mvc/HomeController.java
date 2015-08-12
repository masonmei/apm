package com.baidu.oped.apm.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.config.BasicResponse;
import com.baidu.oped.apm.mvc.vo.AgentStat;

import sun.management.Agent;

/**
 * Created by mason on 8/10/15.
 */
@RestController
@RequestMapping("api")
public class HomeController {

    @Autowired


    @RequestMapping("home")
    public BasicResponse<String> home() {
        return BasicResponse.<String>builder().result("test").build();
    }

    @RequestMapping("stat")
    public BasicResponse<AgentStat> statBasicResponse(){

        AgentStat stat = new AgentStat();

        return BasicResponse.<AgentStat>builder().result(stat).build();
    }
}
