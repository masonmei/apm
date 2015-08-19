//package com.baidu.oped.apm.mvc.controller;
//
//import com.baidu.oped.apm.model.dao.hbase.TestDao;
//import com.baidu.oped.apm.model.dao.hbase.TestPO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * ***** description *****
// * Created with IntelliJ IDEA.
// * User: yangbolin
// * Date: 15/8/15
// * Time: 17:58
// * To change this template use File | Settings | File Templates.
// */
//@RestController
//@RequestMapping("test")
//public class TestController {
//
//    @Autowired
//    private TestDao testDao;
//
//    @RequestMapping(value = {"test"})
//    public String findApplication() {
//        TestPO po = new TestPO();
//        po.setTestName("bbbb");
//        long id = testDao.save(po);
//        System.out.println("id = " +  id);
//        return "aa";
//    }
//
//}
