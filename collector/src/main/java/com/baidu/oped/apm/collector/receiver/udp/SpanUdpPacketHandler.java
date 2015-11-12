package com.baidu.oped.apm.collector.receiver.udp;

import com.baidu.oped.apm.collector.receiver.SpanUdpDispatchHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by mason on 11/12/15.
 */
@Component
public class SpanUdpPacketHandler extends AbstractUDPHandlerFactory {

    @Autowired
    public SpanUdpPacketHandler(SpanUdpDispatchHandler dispatchHandler, TBaseFilterChain filterChain) {
        super(dispatchHandler, filterChain);
    }
}
