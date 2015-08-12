package com.baidu.oped.apm.bootstrap.util;

/**
 * class AntPathMatcher
 *
 * @author meidongxu@baidu.com
 */
public class AntPathMatcher implements PathMatcher {

    private final com.baidu.oped.apm.bootstrap.util.spring.AntPathMatcher springAntMatcher =
            new com.baidu.oped.apm.bootstrap.util.spring.AntPathMatcher();
    private final String pattern;

    public AntPathMatcher(String pattern) {
        if (pattern == null) {
            throw new NullPointerException("pattern must not be null");
        }
        this.pattern = pattern;
        preCreatePatternCache();
    }

    public static boolean isAntStylePattern(String pattern) {
        // copy AntPathMatcher.isPattern(String path);
        return (pattern.indexOf('*') != -1 || pattern.indexOf('?') != -1);
    }

    private void preCreatePatternCache() {
        // dummy call
        this.springAntMatcher.match(pattern, "/");
    }

    @Override
    public boolean isMatched(String path) {
        if (path == null) {
            return false;
        }
        return this.springAntMatcher.match(pattern, path);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AntPathMatcher{");
        sb.append("pattern='").append(pattern).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
