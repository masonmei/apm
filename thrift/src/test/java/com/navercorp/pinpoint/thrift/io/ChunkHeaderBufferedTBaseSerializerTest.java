
package com.baidu.oped.apm.thrift.io;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.junit.Test;

import com.baidu.oped.apm.thrift.dto.TSpanChunk;
import com.baidu.oped.apm.thrift.io.ChunkHeaderBufferedTBaseSerializer;
import com.baidu.oped.apm.thrift.io.ChunkHeaderBufferedTBaseSerializerFlushHandler;
import com.baidu.oped.apm.thrift.io.DefaultTBaseLocator;
import com.baidu.oped.apm.thrift.io.TBaseLocator;
import com.baidu.oped.apm.thrift.io.UnsafeByteArrayOutputStream;

/**
 * class ChunkHeaderBufferedTBaseSerializerTest 
 *
 * @author meidongxu@baidu.com
 */
public class ChunkHeaderBufferedTBaseSerializerTest {
    private static final TBaseLocator DEFAULT_TBASE_LOCATOR = new DefaultTBaseLocator();
    private static final TProtocolFactory DEFAULT_PROTOCOL_FACTORY = new TCompactProtocol.Factory();

    private AtomicInteger flushCounter = new AtomicInteger(0);

    @Test
    public void add() throws TException {
        final int chunkSize = 1024;
        
        UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream();
        ChunkHeaderBufferedTBaseSerializer serializer = new ChunkHeaderBufferedTBaseSerializer(out, DEFAULT_PROTOCOL_FACTORY, DEFAULT_TBASE_LOCATOR);
        serializer.setChunkSize(1024);
        serializer.setFlushHandler(new ChunkHeaderBufferedTBaseSerializerFlushHandler() {

            @Override
            public void handle(byte[] buffer, int offset, int length) {
                flushCounter.incrementAndGet();
            }
        });

        // add and flush
        flushCounter.set(0);
        TSpanChunk chunk = new TSpanMockBuilder().buildChunk(1, 1024);
        serializer.add(chunk);
        assertEquals(1, flushCounter.get());

        // add and flush * 3
        flushCounter.set(0);
        chunk = new TSpanMockBuilder().buildChunk(3, 1024);
        serializer.add(chunk);
        assertEquals(3, flushCounter.get());

        // add
        flushCounter.set(0);
        chunk = new TSpanMockBuilder().buildChunk(3, 10);
        serializer.add(chunk);
        assertEquals(0, flushCounter.get());

        // flush
        flushCounter.set(0);
        serializer.flush();
        assertEquals(1, flushCounter.get());
    }
}