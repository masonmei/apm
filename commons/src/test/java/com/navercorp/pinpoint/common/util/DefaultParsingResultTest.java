
package com.baidu.oped.apm.common.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * class DefaultParsingResultTest 
 *
 * @author meidongxu@baidu.com
 */
public class DefaultParsingResultTest {

    @Test
    public void testId() throws Exception {
        DefaultParsingResult result = new DefaultParsingResult();
        Assert.assertEquals(ParsingResult.ID_NOT_EXIST, result.getId());

        // update
        Assert.assertTrue(result.setId(1));

        // already updated
        Assert.assertFalse(result.setId(1));
    }

}