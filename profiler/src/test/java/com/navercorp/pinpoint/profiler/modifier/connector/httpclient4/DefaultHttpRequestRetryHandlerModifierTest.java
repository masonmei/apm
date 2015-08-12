
package com.baidu.oped.apm.profiler.modifier.connector.httpclient4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;

import com.baidu.oped.apm.test.junit4.BasePinpointTest;

/**
 * class DefaultHttpRequestRetryHandlerModifierTest 
 *
 * @author meidongxu@baidu.com
 */
public class DefaultHttpRequestRetryHandlerModifierTest extends BasePinpointTest {

    @Test
    public void test() {
        DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler();
        IOException iOException = new IOException();
        HttpContext context = new BasicHttpContext();
        
        assertTrue(retryHandler.retryRequest(iOException, 1, context));
        assertTrue(retryHandler.retryRequest(iOException, 2, context));
        assertEquals(2, getCurrentSpanEvents().size());
    }
}
