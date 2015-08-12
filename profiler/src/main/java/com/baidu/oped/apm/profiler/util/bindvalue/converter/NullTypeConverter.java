
package com.baidu.oped.apm.profiler.util.bindvalue.converter;

/**
 * class NullTypeConverter 
 *
 * @author meidongxu@baidu.com
 */
public class NullTypeConverter implements Converter {

    @Override
    public String convert(Object[] args) {
        return "null";
    }
}
