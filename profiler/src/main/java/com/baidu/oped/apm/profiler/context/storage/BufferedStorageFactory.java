
package com.baidu.oped.apm.profiler.context.storage;

import com.baidu.oped.apm.bootstrap.config.ProfilerConfig;
import com.baidu.oped.apm.profiler.AgentInformation;
import com.baidu.oped.apm.profiler.context.SpanChunkFactory;
import com.baidu.oped.apm.profiler.sender.DataSender;

/**
 * class BufferedStorageFactory 
 *
 * @author meidongxu@baidu.com
 */
public class BufferedStorageFactory implements StorageFactory {

    private final DataSender dataSender;
    private final int bufferSize;
    private final SpanChunkFactory spanChunkFactory;

    public BufferedStorageFactory(DataSender dataSender, ProfilerConfig config, AgentInformation agentInformation) {
        if (dataSender == null) {
            throw new NullPointerException("dataSender must not be null");
        }
        if (config == null) {
            throw new NullPointerException("config must not be null");
        }
        this.dataSender = dataSender;

        this.bufferSize = config.getIoBufferingBufferSize();

        this.spanChunkFactory = new SpanChunkFactory(agentInformation);
    }


    @Override
    public Storage createStorage() {
        BufferedStorage bufferedStorage = new BufferedStorage(this.dataSender, spanChunkFactory, this.bufferSize);
        return bufferedStorage;
    }

    @Override
    public String toString() {
        return "BufferedStorageFactory{" +
                "bufferSize=" + bufferSize +
                ", dataSender=" + dataSender +
                '}';
    }
}
