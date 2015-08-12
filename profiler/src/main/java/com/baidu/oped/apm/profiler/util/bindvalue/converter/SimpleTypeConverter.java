
package com.baidu.oped.apm.profiler.util.bindvalue.converter;

import com.baidu.oped.apm.bootstrap.util.StringUtils;

/**
 * class SimpleTypeConverter 
 *
 * @author meidongxu@baidu.com
 */
public class SimpleTypeConverter implements Converter {
    @Override
    public String convert(Object[] args) {
        if (args == null) {
            return "null";
        }
        if (args.length == 2) {
            return StringUtils.drop(StringUtils.toString(args[1]));
        } else if (args.length == 3) {
            // need to handle 3rd arg?
            return StringUtils.drop(StringUtils.toString(args[1]));
        }
        return "error";
    }
}
