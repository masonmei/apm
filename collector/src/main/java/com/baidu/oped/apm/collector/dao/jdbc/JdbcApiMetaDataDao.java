package com.baidu.oped.apm.collector.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.collector.dao.ApiMetaDataDao;
import com.baidu.oped.apm.common.jpa.entity.ApiMetaData;
import com.baidu.oped.apm.common.jpa.repository.ApiMetaDataRepository;
import com.baidu.oped.apm.thrift.dto.TApiMetaData;

/**
 * Created by mason on 8/17/15.
 */
@Component
public class JdbcApiMetaDataDao implements ApiMetaDataDao {
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

        ApiMetaData result = apiMetaDataRepository.findOneByAgentIdAndApiIdAndStartTime(
                                    apiMetaData.getAgentId(), apiMetaData.getApiId(), apiMetaData.getAgentStartTime());
        if (result == null) {
            apiMetaDataRepository.save(metaData);
        }
    }
}