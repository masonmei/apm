
package com.baidu.oped.apm.collector.mapper.thrift;

import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.common.bo.AgentInfoBo;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;

import org.springframework.stereotype.Component;

/**
 * class AgentInfoBoMapper 
 *
 * @author meidongxu@baidu.com
 */



@Component
public class AgentInfoBoMapper implements ThriftBoMapper<AgentInfoBo, TAgentInfo> {

    @Override
    public AgentInfoBo map(TAgentInfo thriftObject) {
        final String hostName = thriftObject.getHostname();
        final String ip = thriftObject.getIp();
        final String ports = thriftObject.getPorts();
        final String agentId = thriftObject.getAgentId();
        final String applicationName = thriftObject.getApplicationName();
        final ServiceType serviceType = ServiceType.findServiceType(thriftObject.getServiceType());
        final int pid = thriftObject.getPid();
        final String version = thriftObject.getVersion();
        final long startTime = thriftObject.getStartTimestamp();
        final long endTimeStamp = thriftObject.getEndTimestamp();
        final int endStatus = thriftObject.getEndStatus();

        AgentInfoBo.Builder builder = new AgentInfoBo.Builder();
        builder.hostName(hostName);
        builder.ip(ip);
        builder.ports(ports);
        builder.agentId(agentId);
        builder.applicationName(applicationName);
        builder.serviceType(serviceType);
        builder.pid(pid);
        builder.version(version);
        builder.startTime(startTime);
        builder.endTimeStamp(endTimeStamp);
        builder.endStatus(endStatus);

        return builder.build();
    }

}
