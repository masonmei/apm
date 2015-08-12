package com.baidu.oped.apm.bootstrap.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * class PluginClassLoader
 *
 * @author meidongxu@baidu.com
 */
public class PluginClassLoader extends URLClassLoader {
    //      for debug
    //    private final String loaderName

    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public PluginClassLoader(URL[] urls) {
        super(urls);
    }

    public PluginClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // for debug
        return super.loadClass(name, resolve);
    }
}
