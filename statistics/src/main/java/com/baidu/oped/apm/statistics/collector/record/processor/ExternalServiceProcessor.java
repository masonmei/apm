package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.AnnotationKey;
import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.Annotation;
import com.baidu.oped.apm.common.jpa.entity.ExternalService;
import com.baidu.oped.apm.common.jpa.entity.ExternalServiceStatistic;
import com.baidu.oped.apm.common.jpa.entity.QAnnotation;
import com.baidu.oped.apm.common.jpa.entity.QExternalService;
import com.baidu.oped.apm.common.jpa.entity.TraceEvent;
import com.baidu.oped.apm.common.jpa.repository.AnnotationRepository;
import com.baidu.oped.apm.common.jpa.repository.ExternalTransactionRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 9/1/15.
 */
@Component
public class ExternalServiceProcessor extends BaseTraceEventProcessor<ExternalServiceStatistic> {

    @Autowired
    private ExternalTransactionRepository externalTransactionRepository;

    @Autowired
    private AnnotationRepository annotationRepository;

    public void setExternalTransactionRepository(ExternalTransactionRepository externalTransactionRepository) {
        this.externalTransactionRepository = externalTransactionRepository;
    }

    @Override
    protected ExternalServiceStatistic newStatisticInstance(EventGroup group) {
        ExternalServiceEventGroup eventGroup = (ExternalServiceEventGroup) group;
        QExternalService qExternalService = QExternalService.externalService;
        BooleanExpression appIdCondition = qExternalService.appId.eq(eventGroup.getAppId());
        BooleanExpression instanceIdCondition = qExternalService.instanceId.eq(eventGroup.getInstanceId());
        BooleanExpression destinationIdCondition = qExternalService.destinationId.eq(eventGroup.getDestinationId());
        BooleanExpression urlCondition = qExternalService.url.eq(eventGroup.getUrl());
        BooleanExpression whereCondition =
                appIdCondition.and(instanceIdCondition).and(destinationIdCondition).and(urlCondition);

        ExternalService one = externalTransactionRepository.findOne(whereCondition);
        if (one == null) {
            ExternalService webTransaction = new ExternalService();
            webTransaction.setAppId(eventGroup.getAppId());
            webTransaction.setInstanceId(eventGroup.getInstanceId());
            webTransaction.setDestinationId(eventGroup.getDestinationId());
            webTransaction.setUrl(eventGroup.getUrl());

            try {
                one = externalTransactionRepository.save(webTransaction);
            } catch (DataAccessException exception) {
                one = externalTransactionRepository.findOne(whereCondition);
            }
        }
        ExternalServiceStatistic webTransactionStatistic = new ExternalServiceStatistic();
        webTransactionStatistic.setExternalServiceId(one.getId());
        return webTransactionStatistic;
    }

    @Override
    protected Map<EventGroup, List<TraceEvent>> groupEvents(Iterable<TraceEvent> items) {
        final Map<Long, AgentInstanceMap> maps = getAgentInstanceMaps(items);
        return StreamSupport.stream(items.spliterator(), false)
                .collect(Collectors.groupingBy(new Function<TraceEvent, ExternalServiceEventGroup>() {
                    @Override
                    public ExternalServiceEventGroup apply(TraceEvent t) {
                        ExternalServiceEventGroup group = new ExternalServiceEventGroup();
                        AgentInstanceMap map = maps.get(t.getAgentId());
                        group.setAppId(map.getAppId());
                        group.setInstanceId(map.getInstanceId());
                        group.setDestinationId(t.getDestinationId());
                        Annotation annotation = getAnnotations(t);
                        if (annotation != null) {
                            annotation.getByteValue();
                            String decode = (String) transcoder.decode((byte) 0, annotation.getByteValue());
                            group.setUrl(decode);
                        }
                        return group;
                    }
                }));
    }

    private Annotation getAnnotations(TraceEvent event) {
        QAnnotation qAnnotation = QAnnotation.annotation;
        BooleanExpression traceEventIdCondition = qAnnotation.traceEventId.eq(event.getId());
        int code = AnnotationKey.HTTP_URL.getCode();
        BooleanExpression keyCondition = qAnnotation.key.eq(code);
        BooleanExpression whereCondition = traceEventIdCondition.and(keyCondition);

        return annotationRepository.findOne(whereCondition);
    }

    class ExternalServiceEventGroup implements EventGroup {
        private Long appId;
        private Long instanceId;
        private String destinationId;
        private String url;

        @Override
        public Long getAppId() {
            return appId;
        }

        public void setAppId(Long appId) {
            this.appId = appId;
        }

        @Override
        public Long getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(Long instanceId) {
            this.instanceId = instanceId;
        }

        public String getDestinationId() {
            return destinationId;
        }

        public void setDestinationId(String destinationId) {
            this.destinationId = destinationId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ExternalServiceEventGroup that = (ExternalServiceEventGroup) o;

            if (appId != null ? !appId.equals(that.appId) : that.appId != null) {
                return false;
            }
            if (instanceId != null ? !instanceId.equals(that.instanceId) : that.instanceId != null) {
                return false;
            }
            if (destinationId != null ? !destinationId.equals(that.destinationId) : that.destinationId != null) {
                return false;
            }
            return !(url != null ? !url.equals(that.url) : that.url != null);

        }

        @Override
        public int hashCode() {
            int result = appId != null ? appId.hashCode() : 0;
            result = 31 * result + (instanceId != null ? instanceId.hashCode() : 0);
            result = 31 * result + (destinationId != null ? destinationId.hashCode() : 0);
            result = 31 * result + (url != null ? url.hashCode() : 0);
            return result;
        }
    }
}
