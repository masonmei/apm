
package com.baidu.oped.apm.collector.receiver.udp;

import com.baidu.oped.apm.collector.config.StatConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * class StatUDPReceiver
 *
 * @author meidongxu@baidu.com
 */
@Component
public class StatUDPReceiver extends AbstractUDPReceiver {

    @Autowired
    public StatUDPReceiver(StatConfig statConfig, StatUdpPacketHandler packetHandlerFactory) {
        super(statConfig, packetHandlerFactory);
    }
}
