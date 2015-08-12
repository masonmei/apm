
package com.baidu.oped.apm.profiler.sender;

import java.util.Collection;

/**
 * class AsyncQueueingExecutorListener 
 *
 * @author meidongxu@baidu.com
 */
public interface AsyncQueueingExecutorListener<T> {

    void execute(Collection<T> messageList);

    void execute(T message);
    
}
