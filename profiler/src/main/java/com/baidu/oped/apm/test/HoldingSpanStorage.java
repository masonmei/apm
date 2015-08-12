
package com.baidu.oped.apm.test;

import org.apache.thrift.TBase;

import com.baidu.oped.apm.profiler.context.Span;
import com.baidu.oped.apm.profiler.context.SpanEvent;
import com.baidu.oped.apm.profiler.context.storage.Storage;

/**
 * class HoldingSpanStorage 
 *
 * @author meidongxu@baidu.com
 */
public final class HoldingSpanStorage implements Storage {

    private final PeekableDataSender<? extends TBase<?, ?>> dataSender;

    public HoldingSpanStorage(PeekableDataSender<? extends TBase<?, ?>> dataSender) {
        this.dataSender = dataSender;
    }

    @Override
    public void store(SpanEvent spanEvent) {
        if (spanEvent == null) {
            throw new NullPointerException("spanEvent must not be null");
        }
        this.dataSender.send(spanEvent);
    }

    @Override
    public void store(Span span) {
        if (span == null) {
            throw new NullPointerException("span must not be null");
        }
        this.dataSender.send(span);
    }

}
