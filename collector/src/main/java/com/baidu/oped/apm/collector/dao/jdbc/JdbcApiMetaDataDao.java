package com.baidu.oped.apm.collector.dao.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.BaseRepository;
import com.baidu.oped.apm.collector.dao.ApiMetaDataDao;
import com.baidu.oped.apm.common.entity.ApiMetaData;
import com.baidu.oped.apm.thrift.dto.TApiMetaData;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcApiMetaDataDao extends BaseRepository<ApiMetaData> implements ApiMetaDataDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcApiMetaDataDao.class);

    @Override
    public void insert(TApiMetaData apiMetaData) {
        if (apiMetaData == null) {
            throw new NullPointerException("apiMetaData must not be null");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("insert:{}", apiMetaData);
        }

        ApiMetaData metaData = new ApiMetaData();
        metaData.setAgentId(apiMetaData.getAgentId());
        metaData.setStartTime(apiMetaData.getAgentStartTime());
        metaData.setApiId(apiMetaData.getApiId());
        if (apiMetaData.isSetLine()) {
            metaData.setLineNumber(apiMetaData.getLine());
        } else {
            metaData.setLineNumber(-1);
        }
        metaData.setApiInfo(apiMetaData.getApiInfo());

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("agentId", apiMetaData.getAgentId());
        conditionMap.put("startTime", apiMetaData.getAgentStartTime());
        ApiMetaData result = findOneByAttrs(conditionMap);
        if (result == null) {
            save(metaData);
        }
    }
}