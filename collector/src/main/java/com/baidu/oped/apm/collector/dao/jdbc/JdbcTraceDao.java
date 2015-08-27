package com.baidu.oped.apm.collector.dao.jdbc;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.baidu.oped.apm.collector.dao.TracesDao;
import com.baidu.oped.apm.collector.util.AcceptedTimeService;
import com.baidu.oped.apm.common.jpa.entity.Annotation;
import com.baidu.oped.apm.common.jpa.entity.QTrace;
import com.baidu.oped.apm.common.jpa.entity.Trace;
import com.baidu.oped.apm.common.jpa.entity.TraceEvent;
import com.baidu.oped.apm.common.jpa.repository.AnnotationRepository;
import com.baidu.oped.apm.common.jpa.repository.TraceEventRepository;
import com.baidu.oped.apm.common.jpa.repository.TraceRepository;
import com.baidu.oped.apm.common.util.AnnotationTranscoder;
import com.baidu.oped.apm.common.util.TransactionId;
import com.baidu.oped.apm.common.util.TransactionIdUtils;
import com.baidu.oped.apm.thrift.dto.TIntStringValue;
import com.baidu.oped.apm.thrift.dto.TSpan;
import com.baidu.oped.apm.thrift.dto.TSpanChunk;
import com.baidu.oped.apm.thrift.dto.TSpanEvent;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/17/15.
 */
@Component
public class JdbcTraceDao implements TracesDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcTraceDao.class);

    private static final AnnotationTranscoder transcoder = new AnnotationTranscoder();

    @Autowired
    private AcceptedTimeService acceptedTimeService;

    @Autowired
    private TraceRepository traceRepository;

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private TraceEventRepository traceEventRepository;

    @Override
    public void insert(TSpan span) {
        if (span == null) {
            throw new NullPointerException("span must not be null");
        }

        Trace trace = new Trace();
        trace.setAgentId(span.getAgentId());
        trace.setApplicationId(span.getApplicationName());
        trace.setAgentStartTime(span.getAgentStartTime());

        final TransactionId transactionId = TransactionIdUtils.parseTransactionId(span.getTransactionId());
        trace.setTraceAgentId(transactionId.getAgentId());
        if (trace.getTraceAgentId() == null) {
            trace.setTraceAgentId(trace.getAgentId());
        }
        trace.setTraceAgentStartTime(transactionId.getAgentStartTime());
        trace.setTraceTransactionSequence(transactionId.getTransactionSequence());

        trace.setSpanId(span.getSpanId());
        trace.setParentSpanId(span.getParentSpanId());

        trace.setStartTime(span.getStartTime());
        trace.setElapsed(span.getElapsed());

        trace.setRpc(span.getRpc());

        trace.setServiceType(span.getServiceType());
        trace.setEndPoint(span.getEndPoint());
        trace.setFlag(span.getFlag());
        trace.setApiId(span.getApiId());

        trace.setErrCode(span.getErr());

        trace.setRemoteAddr(span.getRemoteAddr());

        final TIntStringValue exceptionInfo = span.getExceptionInfo();
        if (exceptionInfo != null) {
            trace.setHasException(true);
            trace.setExceptionId(exceptionInfo.getIntValue());
            trace.setExceptionMessage(exceptionInfo.getStringValue());
        }

        long acceptedTime = acceptedTimeService.getAcceptedTime();
        trace.setCollectorAcceptTime(acceptedTime);

        Trace savedTrace = traceRepository.save(trace);

        if (span.getAnnotations() != null) {
            List<Annotation> annotations =
                    span.getAnnotations().stream().filter(tAnnotation -> tAnnotation != null).map(tAnnotation -> {
                        Annotation annotation = new Annotation();
                        annotation.setKey(tAnnotation.getKey());
                        Object value = transcoder.getMappingValue(tAnnotation);
                        annotation.setValueType(transcoder.getTypeCode(value));
                        annotation.setByteValue(transcoder.encode(value, annotation.getValueType()));
                        annotation.setTraceId(savedTrace.getId());
                        return annotation;
                    }).collect(Collectors.toList());
            annotationRepository.save(annotations);
        }

        addNestedSpanEvent(span, savedTrace);
    }

    @Override
    public void insertSpanChunk(TSpanChunk spanChunk) {
        final byte[] transactionIdBytes = spanChunk.getTransactionId();
        final TransactionId transactionId = TransactionIdUtils.parseTransactionId(transactionIdBytes);

        QTrace qTrace = QTrace.trace;
        BooleanExpression agentIdCondition = qTrace.agentId.eq(transactionId.getAgentId());
        BooleanExpression agentStartTimeCondition = qTrace.agentStartTime.eq(transactionId.getAgentStartTime());
        BooleanExpression spanIdCondition = qTrace.spanId.eq(spanChunk.getSpanId());

        BooleanExpression whereCondition = agentIdCondition.and(agentStartTimeCondition).and(spanIdCondition);
        Trace trace = traceRepository.findOne(whereCondition);

        long acceptedTime = acceptedTimeService.getAcceptedTime();
        spanChunk.getSpanEventList().stream().forEach(spanEvent -> {
            TraceEvent event = new TraceEvent();
            event.setAgentId(trace.getAgentId());
            event.setApplicationId(trace.getApplicationId());
            event.setAgentStartTime(trace.getAgentStartTime());

            event.setTraceAgentId(transactionId.getAgentId());

            if (event.getTraceAgentId() == null) {
                event.setTraceAgentId(event.getAgentId());
            }
            event.setTraceAgentStartTime(transactionId.getAgentStartTime());
            event.setTraceTransactionSequence(transactionId.getTransactionSequence());

            event.setSequence(spanEvent.getSequence());

            event.setStartElapsed(spanEvent.getStartElapsed());
            event.setEndElapsed(spanEvent.getEndElapsed());

            event.setRpc(spanEvent.getRpc());
            event.setServiceType(spanEvent.getServiceType());

            event.setDestinationId(spanEvent.getDestinationId());

            event.setEndPoint(spanEvent.getEndPoint());
            event.setApiId(spanEvent.getApiId());

            if (spanEvent.isSetDepth()) {
                event.setDepth(spanEvent.getDepth());
            }

            if (spanEvent.isSetNextSpanId()) {
                event.setNextSpanId(spanEvent.getNextSpanId());
            }

            final TIntStringValue exceptionInfo = spanEvent.getExceptionInfo();
            if (exceptionInfo != null) {
                event.setHasException(true);
                event.setExceptionId(exceptionInfo.getIntValue());
                event.setExceptionMessage(exceptionInfo.getStringValue());
            }

            event.setCollectorAcceptTime(acceptedTime);

            event.setTraceId(trace.getId());
            TraceEvent savedTraceEvent = traceEventRepository.save(event);

            if (spanEvent.getAnnotations() != null) {
                List<Annotation> annotations =
                        spanEvent.getAnnotations().stream().filter(tAnnotation -> tAnnotation != null)
                                .map(tAnnotation -> {
                                    Annotation annotation = new Annotation();
                                    annotation.setKey(tAnnotation.getKey());
                                    Object value = transcoder.getMappingValue(tAnnotation);
                                    annotation.setValueType(transcoder.getTypeCode(value));
                                    annotation.setByteValue(transcoder.encode(value, annotation.getValueType()));
                                    annotation.setTraceEventId(savedTraceEvent.getTraceId());
                                    return annotation;
                                }).collect(Collectors.toList());
                annotationRepository.save(annotations);
            }
        });
    }

    private void addNestedSpanEvent(TSpan span, Trace trace) {
        Assert.notNull(trace, "trace must not be null while add span event.");

        List<TSpanEvent> spanEventBoList = span.getSpanEventList();
        if (CollectionUtils.isEmpty(spanEventBoList)) {
            return;
        }

        long acceptedTime0 = acceptedTimeService.getAcceptedTime();

        spanEventBoList.stream().filter(spanEvent -> spanEvent != null).forEach(spanEvent -> {
            TraceEvent event = new TraceEvent();
            event.setAgentId(span.getAgentId());
            event.setApplicationId(span.getApplicationName());
            event.setAgentStartTime(span.getAgentStartTime());

            final TransactionId transactionId = TransactionIdUtils.parseTransactionId(span.getTransactionId());
            event.setTraceAgentId(transactionId.getAgentId());

            if (event.getTraceAgentId() == null) {
                event.setTraceAgentId(event.getAgentId());
            }
            event.setTraceAgentStartTime(transactionId.getAgentStartTime());
            event.setTraceTransactionSequence(transactionId.getTransactionSequence());

            event.setSequence(spanEvent.getSequence());

            event.setStartElapsed(spanEvent.getStartElapsed());
            event.setEndElapsed(spanEvent.getEndElapsed());

            event.setRpc(spanEvent.getRpc());
            event.setServiceType(spanEvent.getServiceType());

            event.setDestinationId(spanEvent.getDestinationId());

            event.setEndPoint(spanEvent.getEndPoint());
            event.setApiId(spanEvent.getApiId());

            if (spanEvent.isSetDepth()) {
                event.setDepth(spanEvent.getDepth());
            }

            if (spanEvent.isSetNextSpanId()) {
                event.setNextSpanId(spanEvent.getNextSpanId());
            }

            final TIntStringValue exceptionInfo = spanEvent.getExceptionInfo();
            if (exceptionInfo != null) {
                event.setHasException(true);
                event.setExceptionId(exceptionInfo.getIntValue());
                event.setExceptionMessage(exceptionInfo.getStringValue());
            }

            event.setCollectorAcceptTime(acceptedTime0);

            event.setTraceId(trace.getId());

            TraceEvent saveTraceEvent = traceEventRepository.save(event);

            if (spanEvent.getAnnotations() != null) {
                List<Annotation> annotations =
                        spanEvent.getAnnotations().stream().filter(tAnnotation -> tAnnotation != null)
                                .map(tAnnotation -> {
                                    Annotation annotation = new Annotation();
                                    annotation.setKey(tAnnotation.getKey());
                                    Object value = transcoder.getMappingValue(tAnnotation);
                                    annotation.setValueType(transcoder.getTypeCode(value));
                                    annotation.setByteValue(transcoder.encode(value, annotation.getValueType()));
                                    annotation.setTraceEventId(saveTraceEvent.getTraceId());
                                    return annotation;
                                }).collect(Collectors.toList());
                annotationRepository.save(annotations);
            }
        });
    }

}
