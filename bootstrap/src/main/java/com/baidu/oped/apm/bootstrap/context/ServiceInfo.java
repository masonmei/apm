package com.baidu.oped.apm.bootstrap.context;

import java.util.List;

/**
 * class ServiceInfo
 *
 * @author meidongxu@baidu.com
 */

public interface ServiceInfo {
    String getServiceName();

    List<String> getServiceLibs();
}
