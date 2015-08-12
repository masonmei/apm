
package com.baidu.oped.apm.thrift.io;

/**
 * class ChunkHeaderBufferedTBaseSerializerFlushHandler 
 *
 * @author meidongxu@baidu.com
 */
public interface ChunkHeaderBufferedTBaseSerializerFlushHandler {

    void handle(byte[] buffer, int offset, int length);
}