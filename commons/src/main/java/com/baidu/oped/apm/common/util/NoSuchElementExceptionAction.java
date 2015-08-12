
package com.baidu.oped.apm.common.util;

import java.util.NoSuchElementException;

/**
 * class NoSuchElementExceptionAction 
 *
 * @author meidongxu@baidu.com
 */
public class NoSuchElementExceptionAction<E> implements NextElementAction<E> {

    @Override
    public E nextElement() {
        throw new NoSuchElementException();
    }
}
