package com.baidu.oped.apm.collector.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.collector.dao.SqlMetaDataDao;
import com.baidu.oped.apm.common.jpa.entity.SqlMetaData;
import com.baidu.oped.apm.common.jpa.repository.SqlMetaDataRepository;
import com.baidu.oped.apm.thrift.dto.TSqlMetaData;

/**
 * Created by mason on 8/17/15.
 */
@Component
public class JdbcSqlMetaDataDao  implements SqlMetaDataDao {
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

        SqlMetaData metaData = new SqlMetaData();
        metaData.setAgentId(sqlMetaData.getAgentId());
        metaData.setStartTime(sqlMetaData.getAgentStartTime());
        metaData.setHashCode(sqlMetaData.getSqlId());
        metaData.setSql(sqlMetaData.getSql());

        SqlMetaData result = sqlMetaDataRepository.findOneByAgentIdAndHashCodeAndStartTime(sqlMetaData.getAgentId(),
                                                                                                  sqlMetaData
                                                                                                          .getSqlId(),
                                                                                                  sqlMetaData
                                                                                                          .getAgentStartTime());
        if (result == null) {
            sqlMetaDataRepository.save(metaData);
        }
    }
}