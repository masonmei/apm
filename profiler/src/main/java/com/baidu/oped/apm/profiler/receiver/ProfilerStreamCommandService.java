
package com.baidu.oped.apm.profiler.receiver;

import org.apache.thrift.TBase;

import com.baidu.oped.apm.rpc.stream.ServerStreamChannelContext;

/**
 * class ProfilerStreamCommandService 
 *
 * @author meidongxu@baidu.com
 */


public interface ProfilerStreamCommandService extends ProfilerCommandService {

    short streamCommandService(TBase tBase, ServerStreamChannelContext streamChannelContext);
    
}
