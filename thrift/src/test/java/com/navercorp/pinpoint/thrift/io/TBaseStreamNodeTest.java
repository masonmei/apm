
package com.baidu.oped.apm.thrift.io;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.junit.Test;

import com.baidu.oped.apm.thrift.io.ByteArrayOutputStreamTransport;
import com.baidu.oped.apm.thrift.io.TBaseStreamNode;
import com.baidu.oped.apm.thrift.io.UnsafeByteArrayOutputStream;

/**
 * class TBaseStreamNodeTest 
 *
 * @author meidongxu@baidu.com
 */
public class TBaseStreamNodeTest {

    @Test
    public void size() throws Exception {
        final byte[] buf = "foo".getBytes();

        UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream();
        ByteArrayOutputStreamTransport transport = new ByteArrayOutputStreamTransport(out);
        
        out.write(buf);
        TBaseStreamNode node = new TBaseStreamNode(transport);

        node.setBeginPosition(0);
        node.setEndPosition(buf.length);

        assertEquals(buf.length, node.size());

        ByteArrayOutputStream copy = new ByteArrayOutputStream();
        node.writeTo(copy);

        Arrays.equals(buf, copy.toByteArray());
    }
}