
package com.baidu.oped.apm.profiler.receiver;

import org.apache.thrift.TBase;

/**
 * class ProfilerCommandServiceLocator 
 *
 * @author meidongxu@baidu.com
 */
public interface ProfilerCommandServiceLocator {

    ProfilerCommandService getService(TBase tBase);

    ProfilerSimpleCommandService getSimpleService(TBase tBase);

    ProfilerRequestCommandService getRequestService(TBase tBase);

    ProfilerStreamCommandService getStreamService(TBase tBase);

}
