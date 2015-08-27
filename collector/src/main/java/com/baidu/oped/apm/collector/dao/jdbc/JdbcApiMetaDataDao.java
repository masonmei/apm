package com.baidu.oped.apm.collector.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.collector.dao.ApiMetaDataDao;
import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.ApiMetaData;
import com.baidu.oped.apm.common.jpa.entity.QApiMetaData;
import com.baidu.oped.apm.common.jpa.repository.ApiMetaDataRepository;
import com.baidu.oped.apm.thrift.dto.TApiMetaData;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/17/15.
 */
@Component
public class JdbcApiMetaDataDao extends BaseService implements ApiMetaDataDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcApiMetaDataDao.class);

    @Autowired
    private ApiMetaDataRepository apiMetaDataRepository;

    @Override
    public void insert(TApiMetaData apiMetaData) {
        if (apiMetaData == null) {
            throw new NullPointerException("apiMetaData must not be null");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("insert:{}", apiMetaData);
        }

        AgentInstanceMap map =
                findAgentInstanceMap(apiMetaData.getAgentId(), apiMetaData.getAgentStartTime());
        if (map == null) {
            LOG.warn("AgentInstanceMap not found for agendId {} and startTime {}, this stat data will be ignored",
                            apiMetaData.getAgentId(), apiMetaData.getAgentStartTime());
            return;
        }

        ApiMetaData metaData = new ApiMetaData();
        metaData.setInstanceId(map.getInstanceId());
        metaData.setStartTime(apiMetaData.getAgentStartTime());
        metaData.setApiId(apiMetaData.getApiId());
        if (apiMetaData.isSetLine()) {
            metaData.setLineNumber(apiMetaData.getLine());
        } else {
            metaData.setLineNumber(-1);
        }
        metaData.setApiInfo(apiMetaData.getApiInfo());

        QApiMetaData qApiMetaData = QApiMetaData.apiMetaData;
        BooleanExpression instanceCondition = qApiMetaData.instanceId.eq(map.getInstanceId());
        BooleanExpression apiIdCondition = qApiMetaData.apiId.eq(apiMetaData.getApiId());
        BooleanExpression whereCondition = instanceCondition.and(apiIdCondition);

        ApiMetaData result = apiMetaDataRepository.findOne(whereCondition);
        if (result == null) {
            apiMetaDataRepository.save(metaData);
        }
    }
}