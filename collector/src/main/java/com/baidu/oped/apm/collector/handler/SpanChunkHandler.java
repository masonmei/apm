
package com.baidu.oped.apm.collector.handler;

import com.baidu.oped.apm.collector.dao.TracesDao;
import com.baidu.oped.apm.thrift.dto.TSpanChunk;
import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * class SpanChunkHandler
 *
 * @author meidongxu@baidu.com
 */
@Service
public class SpanChunkHandler implements SimpleHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TracesDao traceDao;

    @Override
    public void handleSimple(TBase<?, ?> tbase) {

        if (!(tbase instanceof TSpanChunk)) {
            throw new IllegalArgumentException("unexpected tbase:" + tbase + " expected:" + this.getClass().getName());
        }

        try {
            TSpanChunk spanChunk = (TSpanChunk) tbase;

            if (logger.isDebugEnabled()) {
                logger.debug("Received SpanChunk={}", spanChunk);
            }

            traceDao.insertSpanChunk(spanChunk);

        } catch (Exception e) {
            logger.warn("SpanChunk handle error Caused:{}", e.getMessage(), e);
        }
    }
}