
package com.baidu.oped.apm.collector.handler;

import java.util.List;

import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.oped.apm.collector.dao.TracesDao;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.common.util.SpanEventUtils;
import com.baidu.oped.apm.thrift.dto.TSpanChunk;
import com.baidu.oped.apm.thrift.dto.TSpanEvent;

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

    @Autowired
    private StatisticsHandler statisticsHandler;

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

            List<TSpanEvent> spanEventList = spanChunk.getSpanEventList();
            if (spanEventList != null) {
                logger.debug("SpanChunk Size:{}", spanEventList.size());
                // TODO need to batch update later.
                for (TSpanEvent spanEvent : spanEventList) {
                    final ServiceType serviceType = ServiceType.findServiceType(spanEvent.getServiceType());

                    if (!serviceType.isRecordStatistics()) {
                        continue;
                    }

                    // if terminal update statistics
                    final int elapsed = spanEvent.getEndElapsed();
                    final boolean hasException = SpanEventUtils.hasException(spanEvent);

                    /**
                     * save information to draw a server map based on statistics
                     */
                    // save the information of caller (the spanevent that span called)
                    statisticsHandler.updateCaller(spanChunk.getApplicationName(), spanChunk.getServiceType(), spanChunk.getAgentId(), spanEvent.getDestinationId(), serviceType.getCode(), spanEvent.getEndPoint(), elapsed, hasException);

                    // save the information of callee (the span that called spanevent)
                    statisticsHandler.updateCallee(spanEvent.getDestinationId(), spanEvent.getServiceType(), spanChunk.getApplicationName(), spanChunk.getServiceType(), spanChunk.getEndPoint(), elapsed, hasException);
                }
            }
        } catch (Exception e) {
            logger.warn("SpanChunk handle error Caused:{}", e.getMessage(), e);
        }
    }
}