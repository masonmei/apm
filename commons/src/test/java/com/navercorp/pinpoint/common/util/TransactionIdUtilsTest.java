
package com.baidu.oped.apm.common.util;

import junit.framework.Assert;

import org.junit.Test;

import com.baidu.oped.apm.common.util.TransactionId;
import com.baidu.oped.apm.common.util.TransactionIdUtils;

/**
 * class TransactionIdUtilsTest 
 *
 * @author meidongxu@baidu.com
 */
public class TransactionIdUtilsTest {
    @Test
    public void testParseTransactionId() {
        TransactionId transactionId = TransactionIdUtils.parseTransactionId("test" + TransactionIdUtils.TRANSACTION_ID_DELIMITER + "1" + TransactionIdUtils.TRANSACTION_ID_DELIMITER + "2");
        Assert.assertEquals(transactionId.getAgentId(), "test");
        Assert.assertEquals(transactionId.getAgentStartTime(), 1L);
        Assert.assertEquals(transactionId.getTransactionSequence(), 2L);
    }

    @Test
    public void testParseTransactionId2() {
        TransactionId transactionId = TransactionIdUtils.parseTransactionId("test" + TransactionIdUtils.TRANSACTION_ID_DELIMITER + "1" + TransactionIdUtils.TRANSACTION_ID_DELIMITER + "2" + TransactionIdUtils.TRANSACTION_ID_DELIMITER);
        Assert.assertEquals(transactionId.getAgentId(), "test");
        Assert.assertEquals(transactionId.getAgentStartTime(), 1L);
        Assert.assertEquals(transactionId.getTransactionSequence(), 2L);
    }

    @Test(expected = Exception.class)
    public void testParseTransactionId_RpcHeaderDuplicateAdd_BugReproduce() {
        // #27 http://yobi.navercorp.com/pinpoint/pinpoint/issue/27
        String id1 = "test" + TransactionIdUtils.TRANSACTION_ID_DELIMITER + "1" + TransactionIdUtils.TRANSACTION_ID_DELIMITER + "2";
        String id2 = "test" + TransactionIdUtils.TRANSACTION_ID_DELIMITER + "1" + TransactionIdUtils.TRANSACTION_ID_DELIMITER + "3";
        TransactionId transactionId = TransactionIdUtils.parseTransactionId(id1 + ", " + id2);
        Assert.assertEquals(transactionId.getAgentId(), "test");
        Assert.assertEquals(transactionId.getAgentStartTime(), 1L);
        Assert.assertEquals(transactionId.getTransactionSequence(), 2L);
    }


    @Test
    public void testParseTransactionIdByte() {
        long time = System.currentTimeMillis();
        byte[] bytes = TransactionIdUtils.formatBytes("test", time, 2);
        TransactionId transactionId = TransactionIdUtils.parseTransactionId(bytes);
        Assert.assertEquals(transactionId.getAgentId(), "test");
        Assert.assertEquals(transactionId.getAgentStartTime(), time);
        Assert.assertEquals(transactionId.getTransactionSequence(), 2L);
    }

    @Test
    public void testParseTransactionIdByte_AgentIdisNull() {
        long time = System.currentTimeMillis();
        byte[] bytes = TransactionIdUtils.formatBytes(null, time, 1);
        TransactionId transactionId = TransactionIdUtils.parseTransactionId(bytes);
        Assert.assertEquals(transactionId.getAgentId(), null);
        Assert.assertEquals(transactionId.getAgentStartTime(), time);
        Assert.assertEquals(transactionId.getTransactionSequence(), 1L);
    }

}
