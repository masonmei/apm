package com.baidu.oped.apm.mvc.controller;

import com.baidu.oped.apm.Application;
import com.baidu.oped.apm.mvc.vo.CallStackVo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mason on 10/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class WebTransactionControllerTest {

    @Autowired
    private WebTransactionController webTransactionController;

    @org.junit.Test
    public void testTransactionCallStack() throws Exception {
        CallStackVo callStackVo = webTransactionController.transactionCallStack(1L, 3L, 8L);
        System.out.println(callStackVo);
    }
}