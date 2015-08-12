package com.baidu.oped.apm.bootstrap.plugin;

/**
 * class MetadataInitializationStrategy
 *
 * @author meidongxu@baidu.com
 */
public interface MetadataInitializationStrategy {
    public static final class ByConstructor implements MetadataInitializationStrategy {
        private final String className;

        public ByConstructor(String className) {
            this.className = className;
        }

        public String getClassName() {
            return className;
        }
    }
}
