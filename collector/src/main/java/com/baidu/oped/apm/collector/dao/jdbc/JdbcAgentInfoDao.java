package com.baidu.oped.apm.collector.dao.jdbc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.AgentInfoDao;
import com.baidu.oped.apm.collector.mapper.thrift.AgentInfoBoMapper;
import com.baidu.oped.apm.collector.mapper.thrift.ServerMetaDataBoMapper;
import com.baidu.oped.apm.common.bo.AgentInfoBo;
import com.baidu.oped.apm.common.bo.ServerMetaDataBo;
import com.baidu.oped.apm.common.bo.ServiceInfoBo;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;

/**
 * Created by mason on 8/15/15.
 */
@Repository
public class JdbcAgentInfoDao extends BaseRepository<AgentInfoBo> implements AgentInfoDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcAgentInfoDao.class);
    private static final String TABLE_NAME = "apm_agent_info";

    @Autowired
    private AgentInfoBoMapper agentInfoBoMapper;

    @Autowired
    private ServerMetaDataBoMapper serverMetaDataBoMapper;

    @Autowired
    private JdbcServerMetaDataDao serverMetaDataDao;

    public JdbcAgentInfoDao() {
        super(AgentInfoBo.class, TABLE_NAME);
    }

    @Override
    public void insert(TAgentInfo agentInfo) {
        if (agentInfo == null) {
            throw new NullPointerException("agentInfo must not be null");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("insert agent info. {}", agentInfo);
        }

        // should add additional agent informations. for now added only starttime for sqlMetaData
        AgentInfoBo agentInfoBo = this.agentInfoBoMapper.map(agentInfo);

        if (agentInfo.isSetServerMetaData()) {
            ServerMetaDataBo serverMetaDataBo = this.serverMetaDataBoMapper.map(agentInfo.getServerMetaData());

            // todo save to database

            List<ServiceInfoBo> serviceInfos = serverMetaDataBo.getServiceInfos();
            if(serviceInfos != null){
                for (ServiceInfoBo serviceInfo : serviceInfos) {

                }
            }
        }

        // TODO save to database;
    }
}
