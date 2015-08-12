
package com.baidu.oped.apm.profiler.modifier.tomcat;

import static org.junit.Assert.*;

import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardService;
import org.apache.catalina.util.ServerInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.baidu.oped.apm.bootstrap.context.ServerMetaData;
import com.baidu.oped.apm.test.junit4.BasePinpointTest;

/**
 * class StandardServiceModifierTest 
 *
 * @author meidongxu@baidu.com
 */



public class StandardServiceModifierTest extends BasePinpointTest {

    private StandardService service;
    
    @Mock
    private StandardEngine engine;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.service = new StandardService();
        this.service.setContainer(this.engine);
    }

    @Test
    public void startShouldCollectServerInfo() throws Exception {
        // Given
        String expectedServerInfo = ServerInfo.getServerInfo();
        // When
        service.start();
        service.stop();
        // Then
        ServerMetaData serverMetaData = getServerMetaData();
        assertEquals(serverMetaData.getServerInfo(), expectedServerInfo);
    }

}
