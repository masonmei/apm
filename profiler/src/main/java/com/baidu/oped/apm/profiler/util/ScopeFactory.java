
package com.baidu.oped.apm.profiler.util;

import com.baidu.oped.apm.bootstrap.instrument.Scope;

/**
 * class ScopeFactory 
 *
 * @author meidongxu@baidu.com
 */
public interface ScopeFactory {

    Scope createScope();

    String getName();

}
