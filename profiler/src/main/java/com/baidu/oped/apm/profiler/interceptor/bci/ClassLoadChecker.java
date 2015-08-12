
package com.baidu.oped.apm.profiler.interceptor.bci;

import com.baidu.oped.apm.profiler.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * class ClassLoadChecker 
 *
 * @author meidongxu@baidu.com
 */
public class ClassLoadChecker {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private static final Object EXIST = new Object();

    private final ConcurrentMap<ClassLoader, ConcurrentMap<String, Object>> classLoaderMap = Maps.newWeakConcurrentMap();


    public boolean exist(ClassLoader classLoader, String className) {
        if (classLoader == null) {
            throw new NullPointerException("classLoader must not be null");
        }
        final ConcurrentMap<String, Object> classMap = findClassMap(classLoader);

        final Object hit = classMap.get(className);
        if (hit != null) {
            if (isDebug) {
                logger.debug("{} already exist from {}", className, classLoader);
            }
            return true;
        }

        final Object old = classMap.putIfAbsent(className, EXIST);
        if (old == null) {
            if (isDebug) {
                logger.debug("{} not exist from {}", className, classLoader);
            }
            return false;
        }
        if (isDebug) {
            logger.debug("{} already exist from {}", className, classLoader);
        }
        return true;
    }

    private ConcurrentMap<String, Object> findClassMap(ClassLoader classLoader) {
        ConcurrentMap<String, Object> hit = this.classLoaderMap.get(classLoader);
        if (hit != null) {
            return hit;
        }
        ConcurrentMap<String, Object> newClassMap = new ConcurrentHashMap<String, Object>();
        ConcurrentMap<String, Object> exist = this.classLoaderMap.putIfAbsent(classLoader, newClassMap);
        if (exist != null) {
            return exist;
        }
        return newClassMap;
    }

}
