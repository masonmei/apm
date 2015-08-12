
package com.baidu.oped.apm.profiler.modifier.connector.httpclient4.interceptor;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;

import com.baidu.oped.apm.bootstrap.instrument.Scope;
import com.baidu.oped.apm.bootstrap.interceptor.TargetClassLoader;
import com.baidu.oped.apm.bootstrap.pair.NameIntValuePair;

/**
 * MethodInfo interceptor
 * <p/>
 * <pre>
 * org.apache.http.impl.client.AbstractHttpClient.
 * public <T> T execute(
 *            final HttpHost target,
 *            final HttpRequest request,
 *            final ResponseHandler<? extends T> responseHandler,
 *            final HttpContext context)
 *            throws IOException, ClientProtocolException {
 * </pre>
 * @author emeroad
 * @author minwoo.jung
 */
public class HttpRequestExecuteInterceptor extends AbstractHttpRequestExecuteWithDivergence implements TargetClassLoader {

    private static final int HTTP_HOST_INDEX = 0;
    private static final int HTTP_REQUEST_INDEX = 1;

    
    public HttpRequestExecuteInterceptor(boolean isHasCallbackParam, Scope scope) {
        super(HttpRequestExecuteInterceptor.class, isHasCallbackParam, scope);
    }
    
    @Override
    protected NameIntValuePair<String> getHost(Object[] args) {
        final Object arg = args[HTTP_HOST_INDEX];
        if (arg instanceof HttpHost) {
            final HttpHost httpHost = (HttpHost) arg;
            return new NameIntValuePair<String>(httpHost.getHostName(), httpHost.getPort());
        }
        return null;
    }

    @Override
    protected HttpRequest getHttpRequest(Object[] args) {
        final Object arg = args[HTTP_REQUEST_INDEX];
        if (arg instanceof HttpRequest) {
            return (HttpRequest) arg;
        }
        return null;
    }
}