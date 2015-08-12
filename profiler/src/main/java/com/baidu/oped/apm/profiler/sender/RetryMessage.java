
package com.baidu.oped.apm.profiler.sender;

/**
 * class RetryMessage 
 *
 * @author meidongxu@baidu.com
 */
public class RetryMessage {
    private int retryCount;
    private byte[] bytes;

    public RetryMessage(int retryCount, byte[] bytes) {
        this.retryCount = retryCount;
        this.bytes = bytes;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int fail() {
        return ++retryCount;
    }
}
