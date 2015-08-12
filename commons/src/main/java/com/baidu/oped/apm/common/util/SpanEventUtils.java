
package com.baidu.oped.apm.common.util;

import com.baidu.oped.apm.thrift.dto.TSpanEvent;

/**
 * class SpanEventUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class SpanEventUtils {

    private SpanEventUtils() {
    }

    public static boolean hasException(TSpanEvent spanEvent) {
        if (spanEvent == null) {
            throw new NullPointerException("spanEvent must not be null");
        }
        if (spanEvent.isSetExceptionInfo()) {
            return true;
        }
        return false;
    }
}
