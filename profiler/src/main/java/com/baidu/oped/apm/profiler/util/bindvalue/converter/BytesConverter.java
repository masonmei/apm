
package com.baidu.oped.apm.profiler.util.bindvalue.converter;

import com.baidu.oped.apm.profiler.util.ArrayUtils;

/**
 * class BytesConverter 
 *
 * @author meidongxu@baidu.com
 */
public class BytesConverter implements Converter {
    @Override
    public String convert(Object[] args) {
        if (args == null) {
            return "null";
        }
        if (args.length == 2) {
            byte[] bytes = (byte[]) args[1];
            if (bytes == null) {
                return "null";
            } else {
                return ArrayUtils.dropToString(bytes);
            }
        }
        return "error";
    }
}
