package com.baidu.oped.apm.collector.dao.jdbc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.baidu.oped.apm.collector.dao.AgentInfoDao;
import com.baidu.oped.apm.common.jpa.entity.AgentInfo;
import com.baidu.oped.apm.common.jpa.entity.ServerMetaData;
import com.baidu.oped.apm.common.jpa.entity.ServiceInfo;
import com.baidu.oped.apm.common.jpa.repository.AgentInfoRepository;
import com.baidu.oped.apm.common.jpa.repository.ServerMetaDataRepository;
import com.baidu.oped.apm.common.jpa.repository.ServiceInfoRepository;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;
import com.baidu.oped.apm.thrift.dto.TServerMetaData;
import com.baidu.oped.apm.thrift.dto.TServiceInfo;

/**
 * Created by mason on 8/15/15.
 */
@Component
public class JdbcAgentInfoDao implements AgentInfoDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AgentInfoRepository agentInfoRepository;

    @Autowired
    private ServerMetaDataRepository serverMetaDataRepository;

    @Autowired
    private ServiceInfoRepository serviceInfoRepository;

    @Override
    public void insert(TAgentInfo agentInfo) {
        if (agentInfo == null) {
            throw new NullPointerException("agentInfo must not be null");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("insert agent info. {}", agentInfo);
        }

        AgentInfo info = convertToAgentInfoEntity(agentInfo);


        if (agentInfo.isSetServerMetaData()) {
            ServerMetaData serverMetaData = new ServerMetaData();
            TServerMetaData thriftObject = agentInfo.getServerMetaData();
            final List<String> vmArgs = thriftObject.getVmArgs();
            serverMetaData.setVmArgs(StringUtils.arrayToDelimitedString(vmArgs.toArray(), ","));

            if (thriftObject.isSetServiceInfos()) {
                for (TServiceInfo tServiceInfo : thriftObject.getServiceInfos()) {
                    String serviceName = tServiceInfo.getServiceName();
                    String serviceLibs = StringUtils.arrayToCommaDelimitedString(tServiceInfo.getServiceLibs().toArray());

                    ServiceInfo serviceInfo = new ServiceInfo();
                    serviceInfo.setServiceName(serviceName);
                    serviceInfo.setServiceLibs(serviceLibs);

                    serverMetaData.addServiceInfo(serviceInfo);
                    serviceInfoRepository.save(serviceInfo);
                }
            }

        }
        agentInfoRepository.save(info);
    }

    private AgentInfo convertToAgentInfoEntity(TAgentInfo agentInfo) {
        AgentInfo info = new AgentInfo();
        info.setHostName(agentInfo.getHostname());
        info.setIp(agentInfo.getIp());
        info.setPorts(agentInfo.getPorts());
        info.setAgentId(agentInfo.getAgentId());
        info.setApplicationName(agentInfo.getApplicationName());
        info.setServiceType(agentInfo.getServiceType());
        info.setPid(agentInfo.getPid());
        info.setVersion(agentInfo.getVersion());
        info.setStartTime(agentInfo.getStartTimestamp());
        info.setEndTimeStamp(agentInfo.getEndTimestamp());
        info.setEndStatus(agentInfo.getEndStatus());
        return info;
    }
}
