
package com.baidu.oped.apm.profiler.monitor.codahale.gc;

import com.baidu.oped.apm.thrift.dto.TJvmGc;
import com.baidu.oped.apm.thrift.dto.TJvmGcType;

/**
 * Unknown Garbage collector
 * @author hyungil.jeong
 */
public class UnknownGarbageCollector implements GarbageCollector {

    public static final TJvmGcType GC_TYPE = TJvmGcType.UNKNOWN;

    @Override
    public int getTypeCode() {
        return GC_TYPE.ordinal();
    }

    @Override
    public TJvmGc collect() {
        // return null to prevent data send.
        // (gc field of Thrift DTO is optional)
        return null;
    }

    @Override
    public String toString() {
        return "Unknown Garbage collector";
    }

}
