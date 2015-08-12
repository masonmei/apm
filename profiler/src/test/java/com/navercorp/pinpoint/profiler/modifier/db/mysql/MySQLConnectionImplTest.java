
package com.baidu.oped.apm.profiler.modifier.db.mysql;

import com.baidu.oped.apm.bootstrap.config.ProfilerConfig;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.profiler.DefaultAgent;
import com.baidu.oped.apm.profiler.logging.Slf4jLoggerBinder;
import com.baidu.oped.apm.test.MockAgent;
import com.baidu.oped.apm.test.TestClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class MySQLConnectionImplTest 
 *
 * @author meidongxu@baidu.com
 */
public class MySQLConnectionImplTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private TestClassLoader loader;

//    @Before
    public void setUp() throws Exception {
        PLoggerFactory.initialize(new Slf4jLoggerBinder());
        DefaultAgent agent = MockAgent.of("pinpoint.config");
        loader = new TestClassLoader(agent);
        loader.initialize();
    }

//    @Test
    public void test() throws Throwable {
        // This is an example of test which loads test class indirectly.  
//        loader.runTest("com.baidu.oped.apm.profiler.modifier.db.mysql.MySQLConnectionImplModifierTest", "testModify");
    }


}
