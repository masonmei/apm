package com.baidu.oped.apm.collector.dao.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.BaseRepository;
import com.baidu.oped.apm.collector.dao.StringMetaDataDao;
import com.baidu.oped.apm.common.entity.StringMetaData;
import com.baidu.oped.apm.thrift.dto.TStringMetaData;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcStringMetaDataDao extends BaseRepository<StringMetaData> implements StringMetaDataDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcStringMetaDataDao.class);

    @Override
    public void insert(TStringMetaData stringMetaData) {
        if (stringMetaData == null) {
            throw new NullPointerException("stringMetaData must not be null");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("insert:{}", stringMetaData);
        }

        StringMetaData metaData = new StringMetaData();
        metaData.setAgentId(stringMetaData.getAgentId());
        metaData.setStartTime(stringMetaData.getAgentStartTime());
        metaData.setStringId(stringMetaData.getStringId());
        metaData.setStringValue(stringMetaData.getStringValue());

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("agentId", stringMetaData.getAgentId());
        conditionMap.put("startTime", stringMetaData.getAgentStartTime());
        conditionMap.put("apiId", stringMetaData.getStringId());
        StringMetaData result = findOneByAttrs(conditionMap);
        if (result == null) {
            save(metaData);
        }
    }
}
