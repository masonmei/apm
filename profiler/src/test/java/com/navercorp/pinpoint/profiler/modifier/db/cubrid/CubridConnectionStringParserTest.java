
package com.baidu.oped.apm.profiler.modifier.db.cubrid;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.profiler.modifier.db.ConnectionStringParser;
import com.baidu.oped.apm.profiler.modifier.db.cubrid.CubridConnectionStringParser;

import junit.framework.Assert;

import org.junit.Test;

/**
 * class CubridConnectionStringParserTest 
 *
 * @author meidongxu@baidu.com
 */
public class CubridConnectionStringParserTest {

    private final ConnectionStringParser parser = new CubridConnectionStringParser();
    @Test
    public void testParse() {
        String cubrid = "jdbc:cubrid:10.99.196.126:34001:nrdwapw:::?charset=utf-8:";

        DatabaseInfo dbInfo = parser.parse(cubrid);

        Assert.assertEquals(dbInfo.getType(), ServiceType.CUBRID);
        Assert.assertEquals(dbInfo.getHost().get(0), "10.99.196.126:34001");
        Assert.assertEquals(dbInfo.getDatabaseId(), "nrdwapw");
        Assert.assertEquals(dbInfo.getUrl(), "jdbc:cubrid:10.99.196.126:34001:nrdwapw:::");
    }

    @Test
    public void testNullParse() {

        DatabaseInfo dbInfo = parser.parse(null);

        Assert.assertEquals(dbInfo.getType(), ServiceType.CUBRID);
        Assert.assertEquals(dbInfo.getHost().get(0), "error");
        Assert.assertEquals(dbInfo.getDatabaseId(), "error");
        Assert.assertEquals(dbInfo.getUrl(), null);

//        Assert.assertEquals(dbInfo.getUrl(), cubrid);
    }
}
