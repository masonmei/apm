
package com.baidu.oped.apm.common.util;

/**
 * class NullNextElementAction 
 *
 * @author meidongxu@baidu.com
 */
public class NullNextElementAction<E> implements NextElementAction<E> {

    @Override
    public E nextElement() {
        return null;
    }
}
