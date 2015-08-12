
package com.baidu.oped.apm.profiler.context.storage;

import com.baidu.oped.apm.profiler.context.Span;
import com.baidu.oped.apm.profiler.context.SpanEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class LogStorageFactory 
 *
 * @author meidongxu@baidu.com
 */
public class LogStorageFactory implements StorageFactory {

    private final static Storage DEFAULT_STORAGE = new LogStorage();

    @Override
    public Storage createStorage() {
         // reuse because it has no states.
        return DEFAULT_STORAGE;
    }

    public static class LogStorage implements Storage {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        @Override
        public void store(SpanEvent spanEvent) {
            logger.debug("log spanEvent:{}", spanEvent);
        }

        @Override
        public void store(Span span) {
            logger.debug("log span:{}", span);
        }
    }
}
