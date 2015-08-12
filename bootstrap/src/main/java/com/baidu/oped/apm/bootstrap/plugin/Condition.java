package com.baidu.oped.apm.bootstrap.plugin;

import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;

/**
 * class Condition
 *
 * @author meidongxu@baidu.com
 */
public interface Condition {
    boolean check(InstrumentClass target);
}
