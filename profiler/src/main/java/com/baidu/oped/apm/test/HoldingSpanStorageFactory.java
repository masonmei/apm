
package com.baidu.oped.apm.test;

import com.baidu.oped.apm.profiler.context.storage.Storage;
import com.baidu.oped.apm.profiler.context.storage.StorageFactory;
import com.baidu.oped.apm.profiler.sender.DataSender;

/**
 * class HoldingSpanStorageFactory 
 *
 * @author meidongxu@baidu.com
 */
public class HoldingSpanStorageFactory implements StorageFactory {

    private final PeekableDataSender<?> dataSender;

    public HoldingSpanStorageFactory(DataSender dataSender) {
        if (dataSender == null) {
            throw new NullPointerException("dataSender must not be null");
        }
        if (dataSender instanceof PeekableDataSender) {
            this.dataSender = (PeekableDataSender<?>)dataSender;
        } else {
            throw new IllegalArgumentException("dataSender must be an instance of PeekableDataSender.");
        }
    }

    @Override
    public Storage createStorage() {
        return new HoldingSpanStorage(this.dataSender);
    }

}
