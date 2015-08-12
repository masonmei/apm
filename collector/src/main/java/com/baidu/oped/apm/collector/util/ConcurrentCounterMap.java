
package com.baidu.oped.apm.collector.util;

import com.baidu.oped.apm.common.util.MathUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * class ConcurrentCounterMap 
 *
 * @author meidongxu@baidu.com
 */
public class ConcurrentCounterMap<T> {

    private final int concurrencyLevel;

    private final AtomicInteger entrySelector;

    private final Entry<T>[] entryArray;

    public ConcurrentCounterMap() {
        this(16);
    }

    public ConcurrentCounterMap(int concurrencyLevel) {
        this(concurrencyLevel, 0);
    }

    public ConcurrentCounterMap(int concurrencyLevel, int entrySelectorId) {
        this.concurrencyLevel = concurrencyLevel;
        this.entryArray = createEntry();
        this.entrySelector = new AtomicInteger(entrySelectorId);
    }

    private Entry<T>[] createEntry() {
        final int concurrencyLevel = this.concurrencyLevel;

        final Entry<T>[] entry = new Entry[concurrencyLevel];
        for (int i = 0; i < entry.length; i++) {
            entry[i] = new Entry<T>();
        }
        return entry;
    }

    private Entry<T> getEntry() {
        final int selectKey = MathUtils.fastAbs(entrySelector.getAndIncrement());
        final int mod = selectKey % concurrencyLevel;
        return entryArray[mod];
    }

    public void increment(T key, Long increment) {
        Entry<T> entry = getEntry();
        entry.increment(key, increment);
    }

    public Map<T, LongAdder> remove() {
        // make a copy of the current snapshot of the entries for consistency
        final List<Map<T, LongAdder>> copy = removeAll();

        // merge
        final Map<T, LongAdder> mergeMap = new HashMap<T, LongAdder>();
        for (Map<T, LongAdder> mutableLongMap : copy) {
            for (Map.Entry<T, LongAdder> entry : mutableLongMap.entrySet()) {
                final T key = entry.getKey();
                LongAdder longAdder = mergeMap.get(key);
                if (longAdder == null) {
                    mergeMap.put(key, entry.getValue());
                } else {
                    longAdder.increment(entry.getValue().get());
                }
            }
        }
        return mergeMap;
    }

    private List<Map<T, LongAdder>> removeAll() {
        final List<Map<T, LongAdder>> copy = new ArrayList<Map<T, LongAdder>>(entryArray.length);
        final int entryArrayLength = entryArray.length;
        for (int i = 0; i < entryArrayLength; i++ ) {
            Entry<T> tEntry = entryArray[i];
            Map<T, LongAdder> remove = tEntry.remove();
            copy.add(remove);
        }
        return copy;
    }


    public static class LongAdder {
        private long value = 0;

        public LongAdder(long increase) {
            this.value = increase;
        }

        public void increment(long increment) {
            this.value += increment;
        }

        public long get() {
            return this.value;
        }
    }

    private static class Entry<T> {
        private static final Map EMPTY = Collections.emptyMap();


        private Map<T, LongAdder> map = new HashMap<T, LongAdder>();

        public synchronized void increment(T key, Long increment) {
            LongAdder longAdder = map.get(key);
            if (longAdder == null) {
                map.put(key, new LongAdder(increment));
            } else {
                longAdder.increment(increment);
            }
        }

        public Map<T, LongAdder> remove() {
            Map<T, LongAdder> old;
            synchronized (this) {
                old = this.map;
                if (old.isEmpty()) {
                    return EMPTY;
                }
                this.map = new HashMap<T, LongAdder>();
            }
            return old;
        }
    }
}
