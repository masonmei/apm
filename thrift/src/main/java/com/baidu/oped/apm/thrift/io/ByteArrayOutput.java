
package com.baidu.oped.apm.thrift.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * write to OutputStream.
 * 
 * @author jaehong.kim
 */
public interface ByteArrayOutput {

    void writeTo(OutputStream out) throws IOException;
}
