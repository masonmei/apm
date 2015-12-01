/*
 * Copyright 2015 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baidu.oped.apm.collector.rpc.handler;


import com.baidu.oped.apm.collector.dao.AgentEventDao;
import com.baidu.oped.apm.collector.receiver.tcp.AgentHandshakePropertyType;
import com.baidu.oped.apm.common.bo.AgentEventBo;
import com.baidu.oped.apm.common.util.AgentEventMessageSerializer;
import com.baidu.oped.apm.common.util.AgentEventType;
import com.baidu.oped.apm.common.util.AgentEventTypeCategory;
import com.baidu.oped.apm.rpc.server.ApmServer;
import com.baidu.oped.apm.rpc.util.MapUtils;
import com.baidu.oped.apm.thrift.io.CommandHeaderTBaseDeserializerFactory;
import com.baidu.oped.apm.thrift.io.DeserializerFactory;
import com.baidu.oped.apm.thrift.io.HeaderTBaseDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @author HyunGil Jeong
 */
@Component
public class AgentEventHandler {

    private static final Set<AgentEventType> RESPONSE_EVENT_TYPES = AgentEventType
            .getTypesByCatgory(AgentEventTypeCategory.USER_REQUEST);

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier(value = "agentEventWorker")
    private Executor executor;

    @Autowired
    private AgentEventDao agentEventDao;

    @Autowired
    private AgentEventMessageSerializer agentEventMessageSerializer;

    @Autowired
    private CommandHeaderTBaseDeserializerFactory commandDeserializerFactory;

    public void handleEvent(ApmServer pinpointServer, long eventTimestamp, AgentEventType eventType) {
        handleEvent(pinpointServer, eventTimestamp, eventType, null);
    }

    public void handleEvent(ApmServer pinpointServer, long eventTimestamp, AgentEventType eventType,
            Object eventMessage) {
        if (pinpointServer == null) {
            throw new NullPointerException("pinpointServer may not be null");
        }
        if (eventType == null) {
            throw new NullPointerException("eventType may not be null");
        }

        Map<Object, Object> channelProperties = pinpointServer.getChannelProperties();

        final String agentId = MapUtils.getString(channelProperties, AgentHandshakePropertyType.AGENT_ID.getName());
        final long startTimestamp = MapUtils.getLong(channelProperties,
                AgentHandshakePropertyType.START_TIMESTAMP.getName());

        this.executor.execute(new AgentEventHandlerDispatch(agentId, startTimestamp, eventTimestamp, eventType,
                eventMessage));
    }

//    public void handleResponseEvent(ResponseEvent responseEvent, long eventTimestamp) {
//        if (responseEvent == null) {
//            throw new NullPointerException("responseEvent may not be null");
//        }
//        TCommandTransferResponse response = responseEvent.getRouteResult();
//        if (response.getRouteResult() != TRouteResult.OK) {
//            return;
//        }
//        this.executor.execute(new AgentResponseEventHandlerDispatch(responseEvent, eventTimestamp));
//    }

    private class AgentEventHandlerDispatch implements Runnable {
        private final String agentId;
        private final long startTimestamp;
        private final long eventTimestamp;
        private final AgentEventType eventType;
        private final Object eventMessage;

        private AgentEventHandlerDispatch(String agentId, long startTimestamp, long eventTimestamp,
                AgentEventType eventType, Object eventMessage) {
            this.agentId = agentId;
            this.startTimestamp = startTimestamp;
            this.eventTimestamp = eventTimestamp;
            this.eventType = eventType;
            this.eventMessage = eventMessage;
        }

        @Override
        public void run() {
            AgentEventBo event = new AgentEventBo(this.agentId, this.startTimestamp,
                    this.eventTimestamp, this.eventType);
            try {
                byte[] eventBody = agentEventMessageSerializer.serialize(this.eventType, this.eventMessage);
                event.setEventBody(eventBody);
            } catch (Exception e) {
                logger.warn("error handling agent event", e);
                return;
            }
            logger.info("handle event: {}", event);
            agentEventDao.insert(event);
        }

    }

//    private class AgentResponseEventHandlerDispatch implements Runnable {
//        private final String agentId;
//        private final long startTimestamp;
//        private final long eventTimestamp;
//        private final byte[] payload;
//
//        private AgentResponseEventHandlerDispatch(ResponseEvent responseEvent, long eventTimestamp) {
//            final TCommandTransfer command = responseEvent.getDeliveryCommand();
//            this.agentId = command.getAgentId();
//            this.startTimestamp = command.getStartTime();
//            this.eventTimestamp = eventTimestamp;
//            final TCommandTransferResponse response = responseEvent.getRouteResult();
//            this.payload = response.getPayload();
//        }
//
//        @Override
//        public void run() {
//            Class<?> payloadType = Void.class;
//            if (this.payload != null) {
//                try {
//                    payloadType = SerializationUtils.deserialize(this.payload, commandDeserializerFactory).getClass();
//                } catch (TException e) {
//                    logger.warn("Error deserializing ResponseEvent payload", e);
//                    return;
//                }
//            }
//            for (AgentEventType eventType : RESPONSE_EVENT_TYPES) {
//                if (eventType.getMessageType() == payloadType) {
//                    AgentEventBo agentEventBo = new AgentEventBo(this.agentId, this.startTimestamp,
//                            this.eventTimestamp, eventType);
//                    agentEventBo.setEventBody(this.payload);
//                    agentEventDao.insert(agentEventBo);
//                }
//            }
//        }
//    }

}
