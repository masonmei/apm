
package com.baidu.oped.apm.profiler.modifier.db;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;

/**
 * class ConnectionStringParser 
 *
 * @author meidongxu@baidu.com
 */
public interface ConnectionStringParser {
    DatabaseInfo parse(String url);
}
