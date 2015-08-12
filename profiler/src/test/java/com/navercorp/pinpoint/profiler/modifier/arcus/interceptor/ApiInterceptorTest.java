
package com.baidu.oped.apm.profiler.modifier.arcus.interceptor;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.MemcachedClient;

import org.junit.Before;
import org.junit.Test;

import com.baidu.oped.apm.bootstrap.interceptor.MethodDescriptor;
import com.baidu.oped.apm.profiler.interceptor.DefaultMethodDescriptor;
import com.baidu.oped.apm.profiler.modifier.arcus.interceptor.ApiInterceptor;
import com.baidu.oped.apm.test.BaseInterceptorTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class ApiInterceptorTest 
 *
 * @author meidongxu@baidu.com
 */
public class ApiInterceptorTest extends BaseInterceptorTest {

    static final Logger logger = LoggerFactory.getLogger(ApiInterceptorTest.class);

    ApiInterceptor interceptor = new ApiInterceptor();
    MemcachedClient client = mock(MockMemcachedClient.class);

    @Before
    public void beforeEach() {
        setInterceptor(interceptor);
        MethodDescriptor methodDescriptor = new DefaultMethodDescriptor(
                MockMemcachedClient.class.getName(), "set", new String[] {
                        "java.lang.String", "int", "java.lang.Object" },
                new String[] { "key", "exptime", "value" });
        /* FIXME NPE. Skip for now.
        setMethodDescriptor(methodDescriptor);
        */
        super.beforeEach();
    }

    @Test
    public void testAround() {
        Object[] args = new Object[] {"key", 10, "my_value"};
        interceptor.before(client, args);
        interceptor.after(client, args, null, null);
    }

    /**
     * Fake MemcachedClient
     */
    class MockMemcachedClient extends MemcachedClient {
        public MockMemcachedClient(ConnectionFactory cf,
                List<InetSocketAddress> addrs) throws IOException {
            super(cf, addrs);
        }

        public String __getServiceCode() {
            return "MEMCACHED";
        }
    }

}