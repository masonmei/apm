package com.baidu.oped.apm.collector.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by mason on 11/12/15.
 */
@Component
public class StatUdpDispatchHandler extends DispatchHandlerWrapper{

    @Autowired
    public StatUdpDispatchHandler(UdpDispatchHandler dispatchHandler) {
        super(dispatchHandler);
    }
}
