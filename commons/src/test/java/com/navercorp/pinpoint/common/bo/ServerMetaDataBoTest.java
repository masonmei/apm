
package com.baidu.oped.apm.common.bo;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.baidu.oped.apm.common.bo.ServerMetaDataBo;
import com.baidu.oped.apm.common.bo.ServiceInfoBo;

/**
 * class ServerMetaDataBoTest 
 *
 * @author meidongxu@baidu.com
 */



public class ServerMetaDataBoTest {

    @Test
    public void testByteArrayConversion() {
        // Given
        final ServerMetaDataBo testBo = createTestBo("testServer", Arrays.asList("arg1", "arg2"), 
                Arrays.asList(ServiceInfoBoTest.createTestBo("testService", Arrays.asList("lib1", "lib2"))));
        // When
        final byte[] serializedBo = testBo.writeValue();
        final ServerMetaDataBo deserializedBo = new ServerMetaDataBo.Builder(serializedBo).build();
        // Then
        assertEquals(testBo, deserializedBo);
    }
    
    @Test
    public void testByteArrayConversionNullValues() {
        // Given
        final ServerMetaDataBo testBo = createTestBo(null, null, null);
        // When
        final byte[] serializedBo = testBo.writeValue();
        final ServerMetaDataBo deserializedBo = new ServerMetaDataBo.Builder(serializedBo).build();
        // Then
        assertEquals(testBo, deserializedBo);
    }
    
    static ServerMetaDataBo createTestBo(String serverInfo, List<String> vmArgs, List<ServiceInfoBo> serviceInfos) {
        final ServerMetaDataBo.Builder builder = new ServerMetaDataBo.Builder();
        builder.serverInfo(serverInfo);
        builder.vmArgs(vmArgs);
        builder.serviceInfos(serviceInfos);
        return builder.build();
    }

}
