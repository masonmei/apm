
package com.baidu.oped.apm.profiler.util.bindvalue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * class Types 
 *
 * @author meidongxu@baidu.com
 */
public class Types {

    private static final Map<Integer, String> MAP;

    static {
        MAP = inverse();
    }

    static Map<Integer, String> inverse() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        Field[] fields = java.sql.Types.class.getFields();
        for (Field field : fields) {
            String name = field.getName();
            try {
                Integer value = (Integer) field.get(java.sql.Types.class);
                map.put(value, name);
            } catch (IllegalAccessException ignore) {
                // skip
            }
        }
        return map;
    }

    public static String findType(int type) {
        return MAP.get(type);
    }
}
