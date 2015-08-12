
package com.baidu.oped.apm.profiler.util;

import com.google.common.collect.MapMaker;

import java.util.concurrent.ConcurrentMap;

/**
 * class Maps 
 *
 * @author meidongxu@baidu.com
 */
public final class Maps {

    private static final MapMaker DEFAULT_WEAK_MAP_MAKER = createWeakMapMaker();

    private static MapMaker createWeakMapMaker() {
        final MapMaker mapMaker = new MapMaker();
        mapMaker.weakKeys();
        return mapMaker;
    }

    public static <K, V> ConcurrentMap<K, V> newWeakConcurrentMap() {
        return DEFAULT_WEAK_MAP_MAKER.makeMap();
    }

    public static <K, V> ConcurrentMap<K, V> newWeakConcurrentMap(int initialCapacity) {
        final MapMaker weakMapMaker = createWeakMapMaker();
        weakMapMaker.initialCapacity(initialCapacity);
        return weakMapMaker.makeMap();
    }
}
