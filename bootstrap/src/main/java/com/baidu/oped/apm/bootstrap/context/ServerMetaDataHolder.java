package com.baidu.oped.apm.bootstrap.context;

import java.util.List;

/**
 * class ServerMetaDataHolder
 *
 * @author meidongxu@baidu.com
 */

public interface ServerMetaDataHolder {
    void setServerName(String serverName);

    void addConnector(String protocol, int port);

    void addServiceInfo(String serviceName, List<String> serviceLibs);

    ServerMetaData getServerMetaData();
}
