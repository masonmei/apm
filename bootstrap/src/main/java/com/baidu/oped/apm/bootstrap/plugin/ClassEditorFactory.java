package com.baidu.oped.apm.bootstrap.plugin;

/**
 * class ClassEditorFactory
 *
 * @author meidongxu@baidu.com
 */
public interface ClassEditorFactory {
    ClassEditor get(ProfilerPluginContext context);
}
