package com.baidu.oped.apm.bootstrap.instrument;

/**
 * class ScopeDefinition
 *
 * @author meidongxu@baidu.com
 */
public interface ScopeDefinition {
    String getName();

    Type getType();

    enum Type {
        SIMPLE, ATTACHMENT
    }
}
