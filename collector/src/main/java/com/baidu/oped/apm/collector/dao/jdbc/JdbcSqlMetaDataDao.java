package com.baidu.oped.apm.collector.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.collector.dao.SqlMetaDataDao;
import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.QSqlMetaData;
import com.baidu.oped.apm.common.jpa.entity.SqlMetaData;
import com.baidu.oped.apm.common.jpa.repository.SqlMetaDataRepository;
import com.baidu.oped.apm.thrift.dto.TSqlMetaData;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/17/15.
 */
@Component
public class JdbcSqlMetaDataDao extends BaseService implements SqlMetaDataDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcSqlMetaDataDao.class);

    @Autowired
    private SqlMetaDataRepository sqlMetaDataRepository;

    @Override
    public void insert(TSqlMetaData sqlMetaData) {
        if (sqlMetaData == null) {
            throw new NullPointerException("sqlMetaData must not be null");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("insert:{}", sqlMetaData);
        }

        AgentInstanceMap map = findAgentInstanceMap(sqlMetaData.getAgentId(), sqlMetaData.getAgentStartTime());
        if (map == null) {
            LOG.warn("AgentInstanceMap not found for agentId {} and startTime {}, this stat data will be ignored",
                            sqlMetaData.getAgentId(), sqlMetaData.getAgentStartTime());
            return;
        }

        SqlMetaData metaData = new SqlMetaData();
        metaData.setInstanceId(map.getInstanceId());
        metaData.setStartTime(sqlMetaData.getAgentStartTime());
        metaData.setSqlId(sqlMetaData.getSqlId());
        metaData.setSql(sqlMetaData.getSql());

        QSqlMetaData qSqlMetaData = QSqlMetaData.sqlMetaData;
        BooleanExpression instanceCondition = qSqlMetaData.instanceId.eq(map.getInstanceId());
        BooleanExpression apiIdCondition = qSqlMetaData.sqlId.eq(sqlMetaData.getSqlId());
        BooleanExpression whereCondition = instanceCondition.and(apiIdCondition);
        SqlMetaData result = sqlMetaDataRepository.findOne(whereCondition);
        if (result == null) {
            sqlMetaDataRepository.save(metaData);
        }
    }
}