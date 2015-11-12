
package com.baidu.oped.apm.collector.handler;

import com.baidu.oped.apm.collector.dao.TracesDao;
import com.baidu.oped.apm.thrift.dto.TSpan;
import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * class SpanHandler 
 *
 * @author meidongxu@baidu.com
 */
@Service
public class SpanHandler implements SimpleHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TracesDao traceDao;

    public void handleSimple(TBase<?, ?> tbase) {

        if (!(tbase instanceof TSpan)) {
            throw new IllegalArgumentException("unexpected tbase:" + tbase + " expected:" + this.getClass().getName());
        }

        try {
            final TSpan span = (TSpan) tbase;
            if (logger.isDebugEnabled()) {
                logger.debug("Received SPAN={}", span);
            }

            traceDao.insert(span);
        } catch (Exception e) {
            logger.warn("Span handle error. Caused:{}. Span:{}",e.getMessage(), tbase, e);
        }
    }

}
