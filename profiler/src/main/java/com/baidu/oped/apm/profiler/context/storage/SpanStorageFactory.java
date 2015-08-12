
package com.baidu.oped.apm.profiler.context.storage;

import com.baidu.oped.apm.profiler.sender.DataSender;

/**
 * class SpanStorageFactory 
 *
 * @author meidongxu@baidu.com
 */
public class SpanStorageFactory implements StorageFactory {

    protected final DataSender dataSender;

    public SpanStorageFactory(DataSender dataSender) {
        if (dataSender == null) {
            throw new NullPointerException("dataSender must not be null");
        }
        this.dataSender = dataSender;
    }

    @Override
    public Storage createStorage() {
        return new SpanStorage(this.dataSender);
    }
}
