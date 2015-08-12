
package com.baidu.oped.apm.profiler.context;


import java.util.List;

import com.baidu.oped.apm.thrift.dto.TSpanChunk;

/**
 * class SpanChunk 
 *
 * @author meidongxu@baidu.com
 */
public class SpanChunk extends TSpanChunk {

    public SpanChunk(List<SpanEvent> spanEventList) {
        if (spanEventList == null) {
            throw new NullPointerException("spanEventList must not be null");
        }
        setSpanEventList((List) spanEventList);
    }
}
