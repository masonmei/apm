
package com.baidu.oped.apm.common.util;

import java.io.*;
import java.util.Properties;

/**
 * class PropertyUtils 
 *
 * @author meidongxu@baidu.com
 */



public final class PropertyUtils {
    public static final String DEFAULT_ENCODING = "UTF-8";

    private static final ClassLoaderUtils.ClassLoaderCallable CLASS_LOADER_CALLABLE = new ClassLoaderUtils.ClassLoaderCallable() {
        @Override
        public ClassLoader getClassLoader() {
            return PropertyUtils.class.getClassLoader();
        }
    };

    public static interface InputStreamFactory {
        InputStream openInputStream() throws IOException;
    }

    private PropertyUtils() {
    }

    public static Properties loadProperty(final String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("filePath must not be null");
        }
        final InputStreamFactory inputStreamFactory = new InputStreamFactory() {
            @Override
            public InputStream openInputStream() throws IOException {
                return new FileInputStream(filePath);
            }
        };
        return loadProperty(new Properties(), inputStreamFactory, DEFAULT_ENCODING);
    }

    public static Properties loadPropertyFromClassPath(final String classPath) throws IOException {
        if (classPath == null) {
            throw new NullPointerException("classPath must not be null");
        }
        final InputStreamFactory inputStreamFactory = new InputStreamFactory() {
            @Override
            public InputStream openInputStream() throws IOException {
                return ClassLoaderUtils.getDefaultClassLoader(CLASS_LOADER_CALLABLE).getResourceAsStream(classPath);
            }
        };
        return loadProperty(new Properties(), inputStreamFactory, DEFAULT_ENCODING);
    }

    public static Properties loadPropertyFromClassLoader(final ClassLoader classLoader, final String classPath) throws IOException {
        if (classLoader == null) {
            throw new NullPointerException("classLoader must not be null");
        }
        final InputStreamFactory inputStreamFactory = new InputStreamFactory() {
            @Override
            public InputStream openInputStream() throws IOException {
                return classLoader.getResourceAsStream(classPath);
            }
        };
        return loadProperty(new Properties(), inputStreamFactory, DEFAULT_ENCODING);
    }


    public static Properties loadProperty(Properties properties, InputStreamFactory inputStreamFactory, String encoding) throws IOException {
        if (properties == null) {
            throw new NullPointerException("properties must not be null");
        }
        if (inputStreamFactory == null) {
            throw new NullPointerException("inputStreamFactory must not be null");
        }
        if (encoding == null) {
            throw new NullPointerException("encoding must not be null");
        }
        InputStream in = null;
        Reader reader = null;
        try {
            in = inputStreamFactory.openInputStream();
            reader = new InputStreamReader(in, encoding);
            properties.load(reader);
        } finally {
            close(reader);
            close(in);
        }
        return properties;
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
                // skip
            }
        }
    }

}
