package com.baidu.oped.apm.bootstrap.instrument;

/**
 * class Scope
 *
 * @author meidongxu@baidu.com
 */
public interface Scope {

    int ZERO = 0;

    int push();

    int depth();

    int pop();

    String getName();

}
