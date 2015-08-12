
package com.baidu.oped.apm.profiler.util;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.profiler.util.RuntimeMXBeanUtils;

import java.util.Date;

/**
 * class RuntimeMXBeanUtilsTest 
 *
 * @author meidongxu@baidu.com
 */
public class RuntimeMXBeanUtilsTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void vmStartTime() {
        long vmStartTime = RuntimeMXBeanUtils.getVmStartTime();
        logger.debug("vmStartTime:{}", new Date(vmStartTime));
        Assert.assertNotSame(vmStartTime, 0);
    }

    @Test
    public void pid() {
        int pid = RuntimeMXBeanUtils.getPid();
        logger.debug("pid:{}", pid);
        Assert.assertTrue(pid > 0);
    }
}
