
package com.baidu.oped.apm.rpc;

/**
 * class Future 
 *
 * @author meidongxu@baidu.com
 */
public interface Future<T> {

    T getResult();

    Throwable getCause();

    boolean isReady();

    boolean isSuccess();

    boolean setListener(FutureListener<T> listener);

    boolean await(long timeoutMillis);

    boolean await();
}
