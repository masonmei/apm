
package com.baidu.oped.apm.collector.receiver;

import com.baidu.oped.apm.collector.handler.SimpleHandler;
import com.baidu.oped.apm.collector.handler.SpanChunkHandler;
import com.baidu.oped.apm.collector.handler.SpanHandler;
import com.baidu.oped.apm.thrift.dto.*;

import org.apache.thrift.TBase;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * class UdpSpanDispatchHandler 
 *
 * @author meidongxu@baidu.com
 */
@Component
@Qualifier("udpSpanDispatchHandler")
public class UdpSpanDispatchHandler extends AbstractDispatchHandler {


    @Autowired()
    private SpanHandler spanDataHandler;


    @Autowired()
    private SpanChunkHandler spanChunkHandler;

    public UdpSpanDispatchHandler() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    @Override
    SimpleHandler getSimpleHandler(TBase<?, ?> tBase) {
        if (tBase instanceof TSpan) {
            return spanDataHandler;
        }
        if (tBase instanceof TSpanChunk) {
            return spanChunkHandler;
        }

        return null;
    }
}
