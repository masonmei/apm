
package com.baidu.oped.apm.profiler.receiver;

import org.apache.thrift.TBase;

/**
 * class ProfilerSimpleCommandService 
 *
 * @author meidongxu@baidu.com
 */
public interface ProfilerSimpleCommandService extends ProfilerCommandService {

    void simpleCommandService(TBase<?, ?> tbase);

}
