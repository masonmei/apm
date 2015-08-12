
package com.baidu.oped.apm.profiler.util;

import com.baidu.oped.apm.bootstrap.instrument.Scope;
import com.baidu.oped.apm.bootstrap.instrument.ScopeDefinition;

/**
 * class ScopePool 
 *
 * @author meidongxu@baidu.com
 */
public interface ScopePool {
    Scope getScope(ScopeDefinition scopeDefinition);
}
