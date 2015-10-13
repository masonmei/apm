package com.baidu.oped.apm.utils;

import com.baidu.oped.apm.common.jpa.entity.Trace;
import com.baidu.oped.apm.mvc.vo.TraceVo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by mason on 10/10/15.
 */
public abstract class TraceUtils {
    public static List<TraceVo> toResponse(Iterable<Trace> traces) {
        return StreamSupport.stream(traces.spliterator(), false).map(TraceUtils::toVo).collect(Collectors.toList());
    }

    public static TraceVo toVo(Trace trace) {
        TraceVo vo = new TraceVo();
        vo.setTraceId(trace.getId());
        vo.setCollectorAcceptTime(trace.getCollectorAcceptTime());
        vo.setElapsed(trace.getElapsed());
        vo.setEndpoint(trace.getEndPoint());
        vo.setRemoteAddr(trace.getRemoteAddr());
        vo.setRpc(trace.getRpc());
        vo.setStartTime(trace.getStartTime());
        vo.setException(trace.isHasException());
        return vo;
    }
}
