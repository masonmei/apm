package com.baidu.oped.apm.bootstrap.pair;

/**
 * class NameValuePair
 *
 * @author meidongxu@baidu.com
 */

/**
 * Class encapsulating a name/value pair. 
 * <p>Used as the common data structure when sharing multiple "value access" data with an interceptor loaded by the
 * parent class loader.
 * @author emeroad
 */
public class NameValuePair<T, V> {
    private T name;
    private V value;

    public NameValuePair(T name, V value) {
        this.name = name;
        this.value = value;
    }

    public T getName() {
        return name;
    }

    public void setName(T name) {
        this.name = name;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
