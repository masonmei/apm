package com.baidu.oped.apm.bootstrap.context;

import java.util.List;
import java.util.Map;

/**
 * class ServerMetaData
 *
 * @author meidongxu@baidu.com
 */

public interface ServerMetaData {
    String getServerInfo();

    List<String> getVmArgs();

    Map<Integer, String> getConnectors();

    List<ServiceInfo> getServiceInfos();
}
