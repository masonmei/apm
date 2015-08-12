package com.baidu.oped.apm.bootstrap.interceptor;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;

/**
 * class LifeCycleEventListener
 *
 * @author meidongxu@baidu.com
 */

public class LifeCycleEventListener {

    private final static PLogger logger = PLoggerFactory.getLogger(LifeCycleEventListener.class.getName());

    private final Agent agent;

    public LifeCycleEventListener(Agent agent) {
        if (agent == null) {
            throw new IllegalArgumentException("agent must not be null");
        }
        this.agent = agent;
    }

    public void start() {
        logger.info("LifeCycleEventListener start");
        agent.start();
    }

    public void stop() {
        logger.info("LifeCycleEventListener stop");
        agent.stop();
    }
}
