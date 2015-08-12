package com.baidu.oped.apm.bootstrap.plugin;

import java.util.List;

/**
 * class ProfilerPlugin
 *
 * @author meidongxu@baidu.com
 */
public interface ProfilerPlugin {
    List<ClassEditor> getClassEditors(ProfilerPluginContext context);
}
