package com.baidu.oped.apm.bootstrap.plugin;

import com.baidu.oped.apm.bootstrap.instrument.InstrumentClass;

/**
 * class ClassEditor
 *
 * @author meidongxu@baidu.com
 */
public interface ClassEditor {
    byte[] edit(ClassLoader classLoader, InstrumentClass target);
}
