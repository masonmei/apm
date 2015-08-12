
package com.baidu.oped.apm.collector.mapper.thrift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.bo.ServerMetaDataBo;
import com.baidu.oped.apm.common.bo.ServiceInfoBo;
import com.baidu.oped.apm.thrift.dto.TServerMetaData;
import com.baidu.oped.apm.thrift.dto.TServiceInfo;

/**
 * class ServerMetaDataBoMapper 
 *
 * @author meidongxu@baidu.com
 */



@Component
public class ServerMetaDataBoMapper implements ThriftBoMapper<ServerMetaDataBo, TServerMetaData> {

    @Override
    public ServerMetaDataBo map(TServerMetaData thriftObject) {
        final String serverInfo = thriftObject.getServerInfo();
        final List<String> vmArgs = thriftObject.getVmArgs();
        ServerMetaDataBo.Builder builder = new ServerMetaDataBo.Builder();
        builder.serverInfo(serverInfo);
        builder.vmArgs(vmArgs);
        if (thriftObject.isSetServiceInfos()) {
            final List<ServiceInfoBo> serviceInfos = new ArrayList<ServiceInfoBo>(thriftObject.getServiceInfosSize());
            for (TServiceInfo tServiceInfo : thriftObject.getServiceInfos()) {
                final ServiceInfoBo serviceInfoBo = mapServiceInfo(tServiceInfo);
                serviceInfos.add(serviceInfoBo);
            }
            builder.serviceInfos(serviceInfos);
            return builder.build();
        } else {
            builder.serviceInfos(Collections.<ServiceInfoBo> emptyList());
            return builder.build();
        }
    }

    private ServiceInfoBo mapServiceInfo(TServiceInfo serviceInfo) {
        final String serviceName = serviceInfo.getServiceName();
        final List<String> serviceLibs = serviceInfo.getServiceLibs();
        return new ServiceInfoBo.Builder().serviceName(serviceName).serviceLibs(serviceLibs).build();
    }

}
