
package com.baidu.oped.apm.profiler.receiver;

import org.apache.thrift.TBase;

/**
 * class ProfilerCommandService 
 *
 * @author meidongxu@baidu.com
 */
public interface ProfilerCommandService {

    Class<? extends TBase> getCommandClazz();

}
