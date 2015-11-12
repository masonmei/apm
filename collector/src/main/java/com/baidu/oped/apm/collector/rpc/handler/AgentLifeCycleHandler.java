package com.baidu.oped.apm.collector.rpc.handler;

import com.baidu.oped.apm.collector.dao.AgentLifeCycleDao;
import com.baidu.oped.apm.common.bo.AgentLifeCycleBo;
import com.baidu.oped.apm.common.util.AgentLifeCycleState;
import com.baidu.oped.apm.common.util.BytesUtils;
import com.baidu.oped.apm.rpc.server.PinpointServer;
import com.baidu.oped.apm.rpc.util.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.Executor;

import static com.baidu.oped.apm.collector.receiver.tcp.AgentHandshakePropertyType.AGENT_ID;
import static com.baidu.oped.apm.collector.receiver.tcp.AgentHandshakePropertyType.START_TIMESTAMP;

/**
 * Created by mason on 11/12/15.
 */
@Component
public class AgentLifeCycleHandler {
    public static final String SOCKET_ID_KEY = "socketId";

    private static final int INTEGER_BIT_COUNT = BytesUtils.INT_BYTE_LENGTH * 8;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier(value = "agentEventWorker")
    private Executor executor;

    @Autowired
    private AgentLifeCycleDao agentLifeCycleDao;

    public void handleLifeCycleEvent(PinpointServer pinpointServer, long eventTimestamp,
                                     AgentLifeCycleState agentLifeCycleState, int eventCounter) {
        if (pinpointServer == null) {
            throw new NullPointerException("pinpointServer may not be null");
        }
        if (agentLifeCycleState == null) {
            throw new NullPointerException("agentLifeCycleState may not be null");
        }
        if (eventCounter < 0) {
            throw new IllegalArgumentException("eventCounter may not be negative");
        }
        logger.info("handle lifecycle event - pinpointServer:{}, state:{}", pinpointServer, agentLifeCycleState);

        Map<Object, Object> channelProperties = pinpointServer.getChannelProperties();
        final Integer socketId = MapUtils.getInteger(channelProperties, SOCKET_ID_KEY);
        if (socketId == null) {
            logger.debug("socketId not found, agent does not support life cycle management - pinpointServer:{}",
                    pinpointServer);
            return;
        }

        final String agentId = MapUtils.getString(channelProperties, AGENT_ID.getName());
        final long startTimestamp = MapUtils.getLong(channelProperties, START_TIMESTAMP.getName());
        final long eventIdentifier = createEventIdentifier(socketId, eventCounter);

        final AgentLifeCycleBo agentLifeCycleBo = new AgentLifeCycleBo(agentId, startTimestamp, eventTimestamp,
                eventIdentifier, agentLifeCycleState);

        this.executor.execute(new AgentLifeCycleHandlerDispatch(agentLifeCycleBo));

    }

    long createEventIdentifier(int socketId, int eventCounter) {
        if (socketId < 0) {
            throw new IllegalArgumentException("socketId may not be less than 0");
        }
        if (eventCounter < 0) {
            throw new IllegalArgumentException("eventCounter may not be less than 0");
        }
        return ((long) socketId << INTEGER_BIT_COUNT) | eventCounter;
    }

    class AgentLifeCycleHandlerDispatch implements Runnable {
        private final AgentLifeCycleBo agentLifeCycleBo;

        private AgentLifeCycleHandlerDispatch(AgentLifeCycleBo agentLifeCycleBo) {
            if (agentLifeCycleBo == null) {
                throw new NullPointerException("agentLifeCycleBo may not be null");
            }
            this.agentLifeCycleBo = agentLifeCycleBo;
        }

        @Override
        public void run() {
            agentLifeCycleDao.insert(this.agentLifeCycleBo);
        }

    }
}
