package com.baidu.oped.apm.collector.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.collector.dao.StringMetaDataDao;
import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.QStringMetaData;
import com.baidu.oped.apm.common.jpa.entity.StringMetaData;
import com.baidu.oped.apm.common.jpa.repository.StringMetaDataRepository;
import com.baidu.oped.apm.thrift.dto.TStringMetaData;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/17/15.
 */
@Component
public class JdbcStringMetaDataDao extends BaseService implements StringMetaDataDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcStringMetaDataDao.class);

    @Autowired
    private StringMetaDataRepository stringMetaDataRepository;
    @Override
    public void insert(TStringMetaData stringMetaData) {
        if (stringMetaData == null) {
            throw new NullPointerException("stringMetaData must not be null");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("insert:{}", stringMetaData);
        }

        AgentInstanceMap map =
                findAgentInstanceMap(stringMetaData.getAgentId(), stringMetaData.getAgentStartTime());
        if (map == null) {
            LOG.warn("AgentInstanceMap not found for agendId {} and startTime {}, this stat data will be ignored",
                            stringMetaData.getAgentId(), stringMetaData.getAgentStartTime());
            return;
        }

        StringMetaData metaData = findStringMetaData(map.getInstanceId(), stringMetaData.getAgentStartTime(),
                                                                   stringMetaData.getStringId());
        metaData.setStringValue(stringMetaData.getStringValue());

        stringMetaDataRepository.saveAndFlush(metaData);
    }
}
