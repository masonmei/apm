
package com.baidu.oped.apm.profiler.context.storage;

import com.baidu.oped.apm.profiler.context.Span;
import com.baidu.oped.apm.profiler.context.SpanEvent;

/**
 * class Storage 
 *
 * @author meidongxu@baidu.com
 */
public interface Storage {

    /**
     *
     * @param spanEvent
     */
    void store(SpanEvent spanEvent);

    /**
     *
     * @param span
     */
    void store(Span span);
}
