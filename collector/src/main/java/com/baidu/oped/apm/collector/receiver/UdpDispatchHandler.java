
package com.baidu.oped.apm.collector.receiver;

import com.baidu.oped.apm.collector.handler.AgentStatHandler;
import com.baidu.oped.apm.collector.handler.Handler;
import com.baidu.oped.apm.thrift.dto.*;

import org.apache.thrift.TBase;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * class UdpDispatchHandler 
 *
 * @author meidongxu@baidu.com
 */
@Component
public class UdpDispatchHandler extends AbstractDispatchHandler {

    @Autowired
    private AgentStatHandler agentStatHandler;

    public UdpDispatchHandler() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    Handler getHandler(TBase<?, ?> tBase) {

        // To change below code to switch table make it a little bit faster.
        // FIXME (2014.08) Legacy - TAgentStats should not be sent over the wire.
        if (tBase instanceof TAgentStat || tBase instanceof TAgentStatBatch) {
            return agentStatHandler;
        }
        return null;
    }

}
