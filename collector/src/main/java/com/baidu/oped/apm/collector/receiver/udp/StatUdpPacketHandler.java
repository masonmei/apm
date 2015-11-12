package com.baidu.oped.apm.collector.receiver.udp;

import com.baidu.oped.apm.collector.receiver.StatUdpDispatchHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by mason on 11/12/15.
 */
@Component
public class StatUdpPacketHandler extends AbstractUDPHandlerFactory {

    @Autowired
    public StatUdpPacketHandler(StatUdpDispatchHandler dispatchHandler, TBaseFilterChain filterChain) {
        super(dispatchHandler, filterChain);
    }
}
