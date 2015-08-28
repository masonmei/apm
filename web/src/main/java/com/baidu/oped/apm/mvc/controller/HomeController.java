//package com.baidu.oped.apm.mvc.controller;
//
//import com.baidu.oped.apm.model.service.ApplicationService;
//import com.baidu.oped.apm.mvc.vo.ApplicationVo;
//import com.baidu.oped.apm.utils.Constaints;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * ***** description *****
// * Created with IntelliJ IDEA.
// * User: yangbolin
// * Date: 15/8/27
// * Time: 17:08
// * To change this template use File | Settings | File Templates.
// */
//@RestController
//@RequestMapping("/api/apm/applications")
//public class HomeController {
//
//    @Autowired
//    ApplicationService applicationService;
//
//    @RequestMapping(method = RequestMethod.GET)
//    public List<ApplicationVo> applications(
//            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
//            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to,
//            @RequestParam(value = "simplify", required = false, defaultValue = "false") boolean simplify,
//            @RequestParam(value = "orderBy") boolean orderBy,
//            @RequestParam(value = "pageSize", required = false, defaultValue = Constaints.PAGE_SIZE) int pageSize,
//            @RequestParam(value = "pageNumber", required = false, defaultValue = Constaints.PAGE_NUMBER) int pageNumber
//    ) {
//        return applicationService.selectApplications(from, to, simplify, orderBy, pageSize, pageNumber);
//    }
//
//}
