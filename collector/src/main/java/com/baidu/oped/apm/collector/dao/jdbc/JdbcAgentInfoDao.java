package com.baidu.oped.apm.collector.dao.jdbc;

import com.baidu.oped.apm.BaseDto;
import com.baidu.oped.apm.JdbcTables;
import com.baidu.oped.apm.collector.dao.AgentInfoDao;
import com.baidu.oped.apm.collector.mapper.thrift.AgentInfoBoMapper;
import com.baidu.oped.apm.collector.mapper.thrift.ServerMetaDataBoMapper;
import com.baidu.oped.apm.common.entity.AgentInfo;
import com.baidu.oped.apm.common.entity.ServerMetaData;
import com.baidu.oped.apm.common.entity.ServiceInfo;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;
import com.baidu.oped.apm.thrift.dto.TServerMetaData;
import com.baidu.oped.apm.thrift.dto.TServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by mason on 8/15/15.
 */
@Repository
public class JdbcAgentInfoDao extends BaseDto<AgentInfo> implements AgentInfoDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AgentInfoBoMapper agentInfoBoMapper;

    @Autowired
    private ServerMetaDataBoMapper serverMetaDataBoMapper;

    @Autowired
    private JdbcServerMetaDataDao jdbcServerMetaDataDao;

    @Autowired
    private JdbcServiceInfoDao jdbcServiceInfoDao;

    @Override
    protected String tableName() {
        return JdbcTables.AGENT_INFO;
    }

    @Override
    public void insert(TAgentInfo agentInfo) {
        if (agentInfo == null) {
            throw new NullPointerException("agentInfo must not be null");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("insert agent info. {}", agentInfo);
        }

        AgentInfo aInfo = new AgentInfo(agentInfo);
        long agentInfoId = this.save(aInfo);

        if (agentInfo.isSetServerMetaData()) {
            ServerMetaData serverMetaData = new ServerMetaData();
            TServerMetaData thriftObject = agentInfo.getServerMetaData();
            final String serverInfo = thriftObject.getServerInfo();
            final List<String> vmArgs = thriftObject.getVmArgs();
            serverMetaData.setServerInfo(serverInfo);
            serverMetaData.setVmArgs(StringUtils.arrayToDelimitedString(vmArgs.toArray(), ","));
            serverMetaData.setAgentId(aInfo.getAgentId());
            serverMetaData.setAgentInfoId(agentInfoId);
            long serverMetaDataId = jdbcServerMetaDataDao.save(serverMetaData);

            if (thriftObject.isSetServiceInfos()) {
                for (TServiceInfo tServiceInfo : thriftObject.getServiceInfos()) {
                    String serviceName = tServiceInfo.getServiceName();
                    String serviceLibs =
                            StringUtils.arrayToCommaDelimitedString(tServiceInfo.getServiceLibs().toArray());

                    ServiceInfo serviceInfo = new ServiceInfo(serverMetaDataId,
                                                                aInfo.getAgentId(), serviceName, serviceLibs);
                    jdbcServiceInfoDao.save(serviceInfo);
                }
            }

        }

    }
}
