
package com.baidu.oped.apm.profiler.interceptor.bci;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class TestObject2 
 *
 * @author meidongxu@baidu.com
 */
public class TestObject2 {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public int callA;
    public int callB;

    public int callA(){
        logger.info("callA");
        int i = callA++;
        return i;
    }

    public void callB() {
        callB++;
    }


}
