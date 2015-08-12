
package com.baidu.oped.apm.profiler.receiver;

import org.apache.thrift.TBase;

/**
 * class ProfilerRequestCommandService 
 *
 * @author meidongxu@baidu.com
 */
public interface ProfilerRequestCommandService extends ProfilerCommandService {

    TBase<?, ?> requestCommandService(TBase tBase);

}
