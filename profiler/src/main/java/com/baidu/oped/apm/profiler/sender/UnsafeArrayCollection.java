
package com.baidu.oped.apm.profiler.sender;

import java.util.AbstractCollection;
import java.util.Iterator;

/**
 * class UnsafeArrayCollection 
 *
 * @author meidongxu@baidu.com
 */
class UnsafeArrayCollection<E> extends AbstractCollection<E> {

    private int size = 0;
    private final Object[] array;

    public UnsafeArrayCollection(int maxSize) {
        this.array = new Object[maxSize];
    }


    @Override
    public boolean add(E o) {
        if (array.length < size) {
            throw new IndexOutOfBoundsException("size:" + this.size + " array.length:" + array.length);
        }
        // do not check array bound
        array[size] = o;
        size++;
        return true;
    }

    @Override
    public void clear() {
        // Need to clear values in array. It costs CPU but prevent memory leak.
        for (int i = 0; i < size; i++) {
            this.array[i] = null;
        }
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        // return internal array
        return array;
    }
}
