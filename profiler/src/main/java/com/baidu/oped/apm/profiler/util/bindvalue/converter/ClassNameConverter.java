
package com.baidu.oped.apm.profiler.util.bindvalue.converter;

import com.baidu.oped.apm.bootstrap.util.StringUtils;

/**
 * class ClassNameConverter 
 *
 * @author meidongxu@baidu.com
 */
public class ClassNameConverter implements Converter {
    @Override
    public String convert(Object[] args) {
        if (args == null) {
            return "null";
        }
        if (args.length == 2) {
            return StringUtils.drop(getClassName(args[1]));
        } else if(args.length == 3) {
           // need to handle 3rd arg?
            return StringUtils.drop(getClassName(args[1]));
        }
        return "error";
    }

    private String getClassName(Object args) {
        if (args == null) {
            return "null";
        }
        return args.getClass().getName();
    }
}
