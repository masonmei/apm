
package com.baidu.oped.apm.thrift.io;

import org.apache.thrift.TBase;

import com.baidu.oped.apm.thrift.dto.TResult;
import com.baidu.oped.apm.thrift.dto.command.TCommandEcho;
import com.baidu.oped.apm.thrift.dto.command.TCommandThreadDump;
import com.baidu.oped.apm.thrift.dto.command.TCommandThreadDumpResponse;
import com.baidu.oped.apm.thrift.dto.command.TCommandTransfer;

/**
 * class TCommandType 
 *
 * @author meidongxu@baidu.com
 */
public enum TCommandType {

    // Using reflection would make code cleaner.
    // But it also makes it hard to handle exception, constructor and will show relatively low performance.

    RESULT((short) 320, TResult.class) {
        @Override
        public TBase newObject() {
            return new TResult();
        }
    },
    TRANSFER((short) 700, TCommandTransfer.class) {
        @Override
        public TBase newObject() {
            return new TCommandTransfer();
        }
    },
    ECHO((short) 710, TCommandEcho.class) {
        @Override
        public TBase newObject() {
            return new TCommandEcho();
        }
    },
    THREAD_DUMP((short) 720, TCommandThreadDump.class) {
        @Override
        public TBase newObject() {
            return new TCommandThreadDump();
        }
    },
    THREAD_DUMP_RESPONSE((short) 721, TCommandThreadDumpResponse.class) {
        @Override
        public TBase newObject() {
            return new TCommandThreadDumpResponse();
        }
    };

    private final short type;
    private final Class<? extends TBase> clazz;
    private final Header header;

    private TCommandType(short type, Class<? extends TBase> clazz) {
        this.type = type;
        this.clazz = clazz;
        this.header = createHeader(type);
    }

    protected short getType() {
        return type;
    }

    protected Class getClazz() {
        return clazz;
    }

    protected boolean isInstanceOf(Object value) {
        return this.clazz.isInstance(value);
    }

    protected Header getHeader() {
        return header;
    }

    public abstract TBase newObject();

    private static Header createHeader(short type) {
        Header header = new Header();
        header.setType(type);
        return header;
    }

}
