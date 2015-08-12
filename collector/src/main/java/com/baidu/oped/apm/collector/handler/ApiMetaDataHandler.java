
package com.baidu.oped.apm.collector.handler;

import com.baidu.oped.apm.collector.dao.ApiMetaDataDao;
import com.baidu.oped.apm.thrift.dto.TApiMetaData;
import com.baidu.oped.apm.thrift.dto.TResult;

import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * class ApiMetaDataHandler 
 *
 * @author meidongxu@baidu.com
 */
@Service
public class ApiMetaDataHandler implements RequestResponseHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiMetaDataDao sqlMetaDataDao;

    @Override
    public TBase<?, ?> handleRequest(TBase<?, ?> tbase) {
        if (!(tbase instanceof TApiMetaData)) {
            logger.error("invalid tbase:{}", tbase);
            return null;
        }

        TApiMetaData apiMetaData = (TApiMetaData) tbase;

        // Because api meta data is important , logging it at info level.
        if (logger.isInfoEnabled()) {
            logger.info("Received ApiMetaData={}", apiMetaData);
        }

        try {
            sqlMetaDataDao.insert(apiMetaData);
        } catch (Exception e) {
            logger.warn("{} handler error. Caused:{}", this.getClass(), e.getMessage(), e);
            TResult result = new TResult(false);
            result.setMessage(e.getMessage());
            return result;
        }
        return new TResult(true);
    }
}
