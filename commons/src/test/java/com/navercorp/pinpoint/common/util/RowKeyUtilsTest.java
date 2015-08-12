
package com.baidu.oped.apm.common.util;

import com.baidu.oped.apm.common.bo.SqlMetaDataBo;

import junit.framework.Assert;

import org.junit.Test;

/**
 * class RowKeyUtilsTest 
 *
 * @author meidongxu@baidu.com
 */
public class RowKeyUtilsTest {
    @Test
    public void testGetSqlId() throws Exception {
        long startTime = System.currentTimeMillis();
        SqlMetaDataBo sqlMetaDataBo = new SqlMetaDataBo("agent", startTime, 1);
        byte[] agents = sqlMetaDataBo.toRowKey();


        SqlMetaDataBo sqlId = new SqlMetaDataBo();
        sqlId.readRowKey(agents);

        Assert.assertEquals(sqlId.getAgentId(), "agent");
        Assert.assertEquals(sqlId.getHashCode(), 1);
        Assert.assertEquals(sqlId.getStartTime(), startTime);
    }
}
