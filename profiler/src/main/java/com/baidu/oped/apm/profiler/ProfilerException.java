
package com.baidu.oped.apm.profiler;

/**
 * class ProfilerException 
 *
 * @author meidongxu@baidu.com
 */
public class ProfilerException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = -4734390009820991000L;

    public ProfilerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfilerException(String message) {
        super(message);
    }

    public ProfilerException(Throwable cause) {
        super(cause);
    }
}
