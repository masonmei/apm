package com.baidu.oped.apm.mvc.vo;

import com.baidu.oped.apm.common.jpa.entity.*;
import com.baidu.oped.apm.mvc.vo.callstack.CallTree;
import com.baidu.oped.apm.mvc.vo.callstack.CallTreeAlign;
import com.baidu.oped.apm.mvc.vo.callstack.CorruptedTreeNodeException;
import com.baidu.oped.apm.mvc.vo.callstack.TraceCallTree;
import com.baidu.oped.apm.utils.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by mason on 10/12/15.
 */
public class CallStackContext {
    private static final Logger LOG = LoggerFactory.getLogger(CallStackContext.class);
    private static final Long ROOT = -1L;

    private Application application;
    private Instance instance;
    private Trace rootTrace;

    private Iterable<Trace> traces;
    private Map<Long, List<Trace>> traceIdMap;
    private Iterable<TraceEvent> traceEvents;
    private Iterable<Annotation> annotations;
    private Iterable<ApiMetaData> apiMetaDatas;

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public void setRootTrace(Trace rootTrace) {
        this.rootTrace = rootTrace;
    }

    public void setTraces(Iterable<Trace> traces) {
        this.traces = traces;
        buildTraceIdMap();
    }

    private void buildTraceIdMap() {
        final Map<Long, List<Trace>> traceMap = new HashMap<>();
        for (Trace trace : traces) {
            final long spanId = trace.getSpanId();
            List<Trace> spanBoList = traceMap.get(spanId);
            if (spanBoList == null) {
                traceMap.put(spanId, new ArrayList<>());
                spanBoList = traceMap.get(spanId);
            }
            spanBoList.add(trace);
        }
        this.traceIdMap = traceMap;
    }

    public void setTraceEvents(Iterable<TraceEvent> traceEvents) {
        this.traceEvents = traceEvents;
    }

    public void setAnnotations(Iterable<Annotation> annotations) {
        this.annotations = annotations;
    }

    public void setApiMetaDatas(Iterable<ApiMetaData> apiMetaDatas) {
        this.apiMetaDatas = apiMetaDatas;
    }

    public CallStackVo build() {
        final CallStackVo callStackVo = new CallStackVo();
        callStackVo.setAppId(application.getId());
        callStackVo.setAppName(application.getAppName());
        callStackVo.setInstanceId(instance.getId());
        callStackVo.setInstanceName(NameUtils.buildInstanceName(instance));
        callStackVo.setBeginTimestamp(rootTrace.getStartTime());

        callStackVo.setCallRecords(buildCallRecords());

        return new CallStackVo();
    }

    private List<CallRecordVo> buildCallRecords() {
//        CallTree tree = populate(rootTrace, getTraceEvent(rootTrace));

        return null;
    }

    private CallTree populate(final Trace trace, final List<TraceEvent> traceEvents) {
        CallTreeAlign align = new CallTreeAlign(trace);
        CallTree callTree = new TraceCallTree(align);

        if (CollectionUtils.isEmpty(traceEvents)) {
            return callTree;
        }

        align.setHasChild(true);

        for (TraceEvent event : traceEvents) {
            final CallTreeAlign eventAlign = new CallTreeAlign(trace, event);

            try {
                callTree.add(event.getDepth(), eventAlign);
            } catch (CorruptedTreeNodeException ex) {
                LOG.warn("Find corrupted span event. ", ex);
                return callTree;
            }

            final long nextSpanId = event.getNextSpanId();
            final List<Trace> nextTraceList = traceIdMap.remove(nextSpanId);
            if (nextSpanId != ROOT && nextTraceList != null) {
                final Trace nextTrace = getNextTrace(trace, event, nextTraceList);
                if (nextTrace != null) {
                    final CallTree subTree = populate(nextTrace, getTraceEvent(nextTrace));
                    callTree.add(subTree);
                } else {
                    LOG.debug("nextTrace with spanId {} not found", nextSpanId);
                }
            }
        }
        LOG.debug("Populate end. Trace: {}", trace);
        return callTree;
    }

    private List<TraceEvent> getTraceEvent(Trace trace) {
        return StreamSupport.stream(traceEvents.spliterator(), false)
                .filter(traceEvent -> traceEvent.getTraceId().equals(trace.getId()))
                .sorted(Comparator.comparing(TraceEvent::getSequence))
                .collect(Collectors.toList());
    }

    private Trace getNextTrace(Trace trace, TraceEvent event, List<Trace> nextTraceList) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("traceEvent:{}, nextTraceList:{}", event, nextTraceList);
        }
        if (nextTraceList.size() == 1) {
            return nextTraceList.get(0);
        } else {
            throw new IllegalStateException("error");
        }
    }
}
