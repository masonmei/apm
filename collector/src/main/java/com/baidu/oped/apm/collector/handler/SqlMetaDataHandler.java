
package com.baidu.oped.apm.collector.handler;

import com.baidu.oped.apm.collector.dao.SqlMetaDataDao;
import com.baidu.oped.apm.thrift.dto.TResult;
import com.baidu.oped.apm.thrift.dto.TSqlMetaData;

import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * class SqlMetaDataHandler 
 *
 * @author meidongxu@baidu.com
 */
@Service
public class SqlMetaDataHandler implements RequestResponseHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("hbaseSqlMetaDataCompatibility")
    private SqlMetaDataDao sqlMetaDataDao;

    @Override
    public TBase<?, ?> handleRequest(TBase<?, ?> tbase) {
        if (!(tbase instanceof TSqlMetaData)) {
            logger.error("invalid tbase:{}", tbase);
            return null;
        }

        TSqlMetaData sqlMetaData = (TSqlMetaData) tbase;

        if (logger.isInfoEnabled()) {
            logger.info("Received SqlMetaData:{}", sqlMetaData);
        }


        try {
            sqlMetaDataDao.insert(sqlMetaData);
        } catch (Exception e) {
            logger.warn("{} handler error. Caused:{}", this.getClass(), e.getMessage(), e);
            TResult result = new TResult(false);
            result.setMessage(e.getMessage());
            return result;
        }
        return new TResult(true);
    }
    
    public void setSqlMetaDataDao(SqlMetaDataDao sqlMetaDataDao) {
        this.sqlMetaDataDao = sqlMetaDataDao;
    }
}
