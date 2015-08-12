package com.baidu.oped.apm.bootstrap.pair;

/**
 * class NameIntValuePair
 *
 * @author meidongxu@baidu.com
 */

/**
 * Class encapsulating a name/value pair.
 * <p>Used as the common data structure when sharing multiple "value access" data with an interceptor loaded by the
 * parent class loader.
 * <p>Use this when value is an "int" type.
 * @author emeroad
 * @see NameValuePair
 */
public class NameIntValuePair<T> {
    private T name;
    private int value;

    public NameIntValuePair(T name, int value) {
        this.name = name;
        this.value = value;
    }

    public T getName() {
        return name;
    }

    public void setName(T name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
