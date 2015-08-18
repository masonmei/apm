package com.baidu.oped.apm.collector.dao.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.BaseRepository;
import com.baidu.oped.apm.collector.dao.SqlMetaDataDao;
import com.baidu.oped.apm.common.entity.SqlMetaData;
import com.baidu.oped.apm.thrift.dto.TSqlMetaData;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcSqlMetaDataDao extends BaseRepository<SqlMetaData> implements SqlMetaDataDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcSqlMetaDataDao.class);

    @Override
    public void insert(TSqlMetaData sqlMetaData) {
        if (sqlMetaData == null) {
            throw new NullPointerException("sqlMetaData must not be null");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("insert:{}", sqlMetaData);
        }

        SqlMetaData metaData = new SqlMetaData();
        metaData.setAgentId(sqlMetaData.getAgentId());
        metaData.setStartTime(sqlMetaData.getAgentStartTime());
        metaData.setHashCode(sqlMetaData.getSqlId());
        metaData.setSql(sqlMetaData.getSql());

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("agentId", sqlMetaData.getAgentId());
        conditionMap.put("startTime", sqlMetaData.getAgentStartTime());
        SqlMetaData result = findOneByAttrs(conditionMap);
        if (result == null) {
            save(metaData);
        }
    }
}