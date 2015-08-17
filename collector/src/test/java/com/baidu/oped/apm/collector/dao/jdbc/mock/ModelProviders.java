package com.baidu.oped.apm.collector.dao.jdbc.mock;

import com.baidu.oped.apm.common.util.TransactionIdUtils;
import com.baidu.oped.apm.thrift.dto.TAnnotation;
import com.baidu.oped.apm.thrift.dto.TAnnotationValue;
import com.baidu.oped.apm.thrift.dto.TSpan;
import com.baidu.oped.apm.thrift.dto.TSpanEvent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mason on 8/17/15.
 */
public abstract class ModelProviders {

    public static final String AGENT_ID = "test-agent";
    public static final long AGENT_START_TIME =
            LocalDateTime.of(2015, 8, 10, 10, 10, 10, 10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    public static final long SPAN_START_TIME =
            LocalDateTime.of(2015, 8, 10, 10, 10, 15, 10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    public static final String APPLICATION_NAME = "TESTAPP";
    public static final byte[] TRANSACTION_ID = TransactionIdUtils.formatBytes(null, AGENT_START_TIME, 1);
    public static final long SPAN_ID = 3044242050217653448l;
    public static final int ELAPSED = 2339;
    public static final String RPC = "/callSelf/getCurrentTimestamp.pinpoint";
    public static final short SERVICE_TYPE = 1010;
    public static final String END_POINT = "localhost:28081";
    public static final String REMOTE_ADDR = "0:0:0:0:0:0:0:1";
    public static final short FLAG = 0;
    public static final int API_ID = -1;

    public static TSpan tSpan() {
        TSpan span = new TSpan();
        span.setAgentId(AGENT_ID);
        span.setAgentStartTime(AGENT_START_TIME);
        span.setApplicationName(APPLICATION_NAME);
        span.setTransactionId(TRANSACTION_ID);
        span.setSpanId(SPAN_ID);
        span.setStartTime(SPAN_START_TIME);
        span.setElapsed(ELAPSED);
        span.setRpc(RPC);
        span.setServiceType(SERVICE_TYPE);
        span.setEndPoint(END_POINT);
        span.setRemoteAddr(REMOTE_ADDR);
        span.setFlag(FLAG);
        span.setApiId(API_ID);

        List<TSpanEvent> spanEventList = new ArrayList<>();
        span.setSpanEventList(spanEventList);

        TSpanEvent spanEvent1 = new TSpanEvent();
        spanEvent1.setSequence((short) 2);
        spanEvent1.setStartElapsed(1931);
        spanEvent1.setEndElapsed(237);
        spanEvent1.setServiceType((short) 9050);
        spanEvent1.setDepth(3);
        spanEvent1.setNextSpanId(9018434346136141568l);
        spanEvent1.setDestinationId("172.21.206.239:28081");
        spanEvent1.setApiId(11);
        List<TAnnotation> annotations = new ArrayList<>();

        TAnnotation annotation = new TAnnotation();
        annotation.setKey(40);
        TAnnotationValue value = new TAnnotationValue();
        value.setStringValue("http://172.21.206.239:28081/getCurrentTimestamp.pinpoint");
        annotation.setValue(value);
        annotations.add(annotation);

        TAnnotation annotation1 = new TAnnotation();
        annotation1.setKey(46);
        TAnnotationValue value1 = new TAnnotationValue();
        value1.setIntValue(200);
        annotation1.setValue(value1);
        annotations.add(annotation1);

        spanEvent1.setAnnotations(annotations);
        spanEventList.add(spanEvent1);
        TSpanEvent spanEvent2 = new TSpanEvent();
        spanEventList.add(spanEvent2);
        TSpanEvent spanEvent3 = new TSpanEvent();
        spanEventList.add(spanEvent3);

        return span;
    }
}
