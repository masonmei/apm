
package com.baidu.oped.apm.profiler.modifier.arcus;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.baidu.oped.apm.bootstrap.instrument.MethodFilter;
import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;

/**
 * class MemcachedMethodFilter 
 *
 * @author meidongxu@baidu.com
 */
public class MemcachedMethodFilter implements MethodFilter {
    private final static Object FIND = new Object();
    private final static Map<String, Object> WHITE_LIST_API;

    static {
        WHITE_LIST_API = createRule();
    }

    private static Map<String, Object> createRule() {
        String[] apiList = {
                "add",
                "append",
                "asyncCAS",
                "asyncDecr",
                "asyncGet",
                "asyncGetBulk",
                "asyncGets",
                "asyncIncr",
                "cas",
                "decr",
                "delete",
                "get",
                "getBulk",
                "gets",
                "incr",
                "prepend",
                "replace",
                "set"
        };
        Map<String, Object> rule = new HashMap<String, Object>();
        for (String api : apiList) {
            rule.put(api, FIND);
        }
        return rule;
    }

    public MemcachedMethodFilter() {
    }

    @Override
    public boolean filter(MethodInfo ctMethod) {
        final int modifiers = ctMethod.getModifiers();
        if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers) || Modifier.isAbstract(modifiers) || Modifier.isNative(modifiers)) {
            return true;
        }
        if (WHITE_LIST_API.get(ctMethod.getName()) == FIND) {
            return false;
        }
        return true;
    }
}
