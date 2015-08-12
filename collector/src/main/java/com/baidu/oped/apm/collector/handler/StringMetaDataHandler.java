
package com.baidu.oped.apm.collector.handler;

import com.baidu.oped.apm.collector.dao.StringMetaDataDao;
import com.baidu.oped.apm.thrift.dto.TResult;
import com.baidu.oped.apm.thrift.dto.TStringMetaData;

import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * class StringMetaDataHandler 
 *
 * @author meidongxu@baidu.com
 */
@Service
public class StringMetaDataHandler implements RequestResponseHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringMetaDataDao stringMetaDataDao;

    @Override
    public TBase<?, ?> handleRequest(TBase<?, ?> tbase) {
        if (!(tbase instanceof TStringMetaData)) {
            logger.error("invalid tbase:{}", tbase);
            return null;
        }

        TStringMetaData stringMetaData = (TStringMetaData) tbase;
        // because api data is important, logging it at info level
        if (logger.isInfoEnabled()) {
            logger.info("Received StringMetaData={}", stringMetaData);
        }

        try {
            stringMetaDataDao.insert(stringMetaData);
        } catch (Exception e) {
            logger.warn("{} handler error. Caused:{}", this.getClass(), e.getMessage(), e);
            TResult result = new TResult(false);
            result.setMessage(e.getMessage());
            return result;
        }
        return new TResult(true);
    }
}
