package com.baidu.oped.apm.mvc.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.mvc.vo.TransactionSummary;

/**
 * Created by mason on 8/13/15.
 */
@RestController
@RequestMapping("transaction")
public class TransactionController {

    @RequestMapping(value = {"top"})
    public List<TransactionSummary> top(
            @RequestParam("appName") String applicationName,
            @RequestParam(value = "number", required = false, defaultValue = "5") int number,
            @RequestParam(value = "instanceId", required = false) String instanceId) {

        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @RequestMapping(value = {"list"})
    public List<TransactionSummary> list(
            @RequestParam("appName") String applicationName,
            @RequestParam(value = "instanceId", required = false) String instanceId) {

        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
