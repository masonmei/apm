package com.baidu.oped.apm.bootstrap.interceptor;

/**
 * class TargetClassLoader
 *
 * @author meidongxu@baidu.com
 */

/**
 * <p>Marker interface for marking interceptors that should be created through 
 * {@code ByteCodeInstrumentor.newInterceptor}.
 * <p>An {@code InstrumentException} is thrown if an interceptor is created through {@code ByteCodeInstrumentor
 * .newInterceptor}
 * without implementing this interface.
 *
 * @author emeroad
 */
public interface TargetClassLoader {
}
