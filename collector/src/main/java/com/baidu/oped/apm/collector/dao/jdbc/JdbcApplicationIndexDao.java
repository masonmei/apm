package com.baidu.oped.apm.collector.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.BaseRepository;
import com.baidu.oped.apm.collector.dao.ApplicationIndexDao;
import com.baidu.oped.apm.common.entity.ApplicationIndex;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcApplicationIndexDao extends BaseRepository<ApplicationIndex> implements ApplicationIndexDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcApplicationIndexDao.class);

    @Override
    public void insert(TAgentInfo agentInfo) {
        if (agentInfo == null) {
            throw new NullPointerException("agentInfo must not be null");
        }

        ApplicationIndex index = new ApplicationIndex();
        index.setApplicationName(agentInfo.getApplicationName());
        index.setAgentId(agentInfo.getAgentId());
        index.setServiceType(agentInfo.getServiceType());

        save(index);

        LOG.debug("Insert agentInfo. {}", agentInfo);
    }
}
