
package com.baidu.oped.apm.test.junit4;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TBase;
import org.junit.runner.RunWith;

import com.baidu.oped.apm.bootstrap.context.ServerMetaData;
import com.baidu.oped.apm.bootstrap.context.ServerMetaDataHolder;
import com.baidu.oped.apm.common.bo.SpanBo;
import com.baidu.oped.apm.common.bo.SpanEventBo;
import com.baidu.oped.apm.profiler.context.Span;
import com.baidu.oped.apm.profiler.context.SpanEvent;
import com.baidu.oped.apm.test.PeekableDataSender;

/**
 * class BasePinpointTest 
 *
 * @author meidongxu@baidu.com
 */



@RunWith(value = PinpointJUnit4ClassRunner.class)
public abstract class BasePinpointTest {
    private ThreadLocal<PeekableDataSender<? extends TBase<?, ?>>> traceHolder = new ThreadLocal<PeekableDataSender<? extends TBase<?, ?>>>();
    private ThreadLocal<ServerMetaDataHolder> serverMetaDataHolder = new ThreadLocal<ServerMetaDataHolder>();

    protected final List<SpanEventBo> getCurrentSpanEvents() {
        List<SpanEventBo> spanEvents = new ArrayList<SpanEventBo>();
        for (TBase<?, ?> span : this.traceHolder.get()) {
            if (span instanceof SpanEvent) {
                SpanEvent spanEvent = (SpanEvent)span;
                spanEvents.add(new SpanEventBo(spanEvent.getSpan(), spanEvent));
            }
        }
        return spanEvents;
    }

    protected final List<SpanBo> getCurrentRootSpans() {
        List<SpanBo> rootSpans = new ArrayList<SpanBo>();
        for (TBase<?, ?> span : this.traceHolder.get()) {
            if (span instanceof Span) {
                rootSpans.add(new SpanBo((Span)span));
            }
        }
        return rootSpans;
    }
    
    protected final ServerMetaData getServerMetaData() {
        return this.serverMetaDataHolder.get().getServerMetaData();
    }

    final void setCurrentHolder(PeekableDataSender<? extends TBase<?, ?>> dataSender) {
        traceHolder.set(dataSender);
    }
    
    final void setServerMetaDataHolder(ServerMetaDataHolder metaDataHolder) {
        this.serverMetaDataHolder.set(metaDataHolder);
    }
}
