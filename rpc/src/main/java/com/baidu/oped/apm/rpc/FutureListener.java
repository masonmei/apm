
package com.baidu.oped.apm.rpc;

/**
 * class FutureListener 
 *
 * @author meidongxu@baidu.com
 */
public interface FutureListener<T> {
    void onComplete(Future<T> future);
}
