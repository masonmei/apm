
package com.baidu.oped.apm.collector.receiver.udp;

import com.baidu.oped.apm.collector.config.SpanConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * class BaseUDPReceiver
 *
 * @author meidongxu@baidu.com
 */
@Component
public class SpanUDPReceiver extends AbstractUDPReceiver {

    @Autowired
    public SpanUDPReceiver(SpanConfig spanConfig, SpanUdpPacketHandler packetHandlerFactory) {
        super(spanConfig, packetHandlerFactory);
    }
}
