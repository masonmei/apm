
package com.baidu.oped.apm.profiler.modifier.db.oracle.parser;

import com.baidu.oped.apm.exception.PinpointException;

/**
 * class OracleConnectionStringException 
 *
 * @author meidongxu@baidu.com
 */
public class OracleConnectionStringException extends PinpointException {

    public OracleConnectionStringException() {
    }

    public OracleConnectionStringException(String message) {
        super(message);
    }

    public OracleConnectionStringException(String message, Throwable cause) {
        super(message, cause);
    }

    public OracleConnectionStringException(Throwable cause) {
        super(cause);
    }
}
