
package com.baidu.oped.apm.profiler.monitor.codahale.gc;

import com.baidu.oped.apm.thrift.dto.TJvmGc;

/**
 * class GarbageCollector 
 *
 * @author meidongxu@baidu.com
 */
public interface GarbageCollector {

    int getTypeCode();

    TJvmGc collect();

}
