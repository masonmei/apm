package com.baidu.oped.apm.bootstrap.config;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

/**
 * class DumpTypeTest
 *
 * @author meidongxu@baidu.com
 */
public class DumpTypeTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void find() {
        DumpType none = DumpType.valueOf("ALWAYS");
        logger.debug("type:{}", none);

        try {
            DumpType.valueOf("error");
            Assert.fail("not found");
        } catch (IllegalArgumentException e) {

        }
    }
}
