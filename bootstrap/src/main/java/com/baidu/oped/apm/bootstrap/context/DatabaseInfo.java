package com.baidu.oped.apm.bootstrap.context;

import java.util.List;

import com.baidu.oped.apm.common.ServiceType;

/**
 * class DatabaseInfo
 *
 * @author meidongxu@baidu.com
 */
public interface DatabaseInfo {
    List<String> getHost();

    String getMultipleHost();

    String getDatabaseId();

    String getRealUrl();

    String getUrl();

    ServiceType getType();

    ServiceType getExecuteQueryType();
}
