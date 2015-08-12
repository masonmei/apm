
package com.baidu.oped.apm.collector.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * class AtomicLongUpdateMap 
 *
 * @author meidongxu@baidu.com
 */
public class AtomicLongUpdateMap<T> {
    // FIXME consider to save a mapping information at each 30 ~ 50 seconds not to do at each time.
    // consider to change to LRU due to OOM risk

    private final ConcurrentMap<T, AtomicLong> cache = new ConcurrentHashMap<T, AtomicLong>(1024, 0.75f, 32);


    public boolean update(final T cacheKey, final long time) {
        if (cacheKey == null) {
            throw new NullPointerException("cacheKey must not be null");
        }
        final AtomicLong hitSlot = cache.get(cacheKey);
        if (hitSlot == null ) {
            final AtomicLong newTime = new AtomicLong(time);
            final AtomicLong oldTime = cache.putIfAbsent(cacheKey, newTime);
            if (oldTime == null) {

                return true;
            } else {
                // the cachekey already exists
                return updateTime(time, oldTime);
            }
        } else {
            // update if the cachekey already exists
            return updateTime(time, hitSlot);
        }
    }

    private boolean updateTime(final long newTime, final AtomicLong oldTime) {
        final long oldLong = oldTime.get();
        if (newTime > oldLong) {
            return oldTime.compareAndSet(oldLong, newTime);
        }
        return false;
    }
}
