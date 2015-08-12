
package com.baidu.oped.apm.thrift.io;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;

/**
 * class CommandHeaderTBaseSerializerFactory 
 *
 * @author meidongxu@baidu.com
 */



public final class CommandHeaderTBaseSerializerFactory implements SerializerFactory<HeaderTBaseSerializer> {

    public static final int DEFAULT_SERIALIZER_MAX_SIZE = 1024 * 64;

    private final SerializerFactory<HeaderTBaseSerializer> factory;

    public CommandHeaderTBaseSerializerFactory(String version) {
        this(version, DEFAULT_SERIALIZER_MAX_SIZE);
    }

    public CommandHeaderTBaseSerializerFactory(String version, int outputStreamSize) {
        TBaseLocator commandTbaseLocator = new TCommandRegistry(TCommandTypeVersion.getVersion(version));

        TProtocolFactory protocolFactory = new TCompactProtocol.Factory();
        HeaderTBaseSerializerFactory serializerFactory = new HeaderTBaseSerializerFactory(true, outputStreamSize, protocolFactory, commandTbaseLocator);

        this.factory = new ThreadLocalHeaderTBaseSerializerFactory<HeaderTBaseSerializer>(serializerFactory);
    }

    @Override
    public HeaderTBaseSerializer createSerializer() {
        return this.factory.createSerializer();
    }

}
