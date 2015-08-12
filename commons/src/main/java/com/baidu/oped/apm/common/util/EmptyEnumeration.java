
package com.baidu.oped.apm.common.util;

import java.util.Enumeration;

/**
 * class EmptyEnumeration 
 *
 * @author meidongxu@baidu.com
 */
public class EmptyEnumeration<E> implements Enumeration<E> {
    private static final NullNextElementAction NULL_NEXT_ELEMENT_ACTION = new NullNextElementAction();

    private final NextElementAction<E> nextElementAction;

    @SuppressWarnings("unchecked")
    public EmptyEnumeration() {
        this(NULL_NEXT_ELEMENT_ACTION);
    }

    public EmptyEnumeration(NextElementAction<E> nextElementAction) {
        this.nextElementAction = nextElementAction;
    }

    @Override
    public boolean hasMoreElements() {
        return false;
    }

    @Override
    public E nextElement() {
        return nextElementAction.nextElement();
    }
}
