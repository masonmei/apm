
package com.baidu.oped.apm.profiler.context;

import java.util.Collections;
import java.util.List;

import com.baidu.oped.apm.bootstrap.context.ServiceInfo;

/**
 * class DefaultServiceInfo 
 *
 * @author meidongxu@baidu.com
 */



public class DefaultServiceInfo implements ServiceInfo {

    private final String serviceName;
    private final List<String> serviceLibs;

    public DefaultServiceInfo(String serviceName, List<String> serviceLibs) {
        if (serviceName == null) {
            this.serviceName = "";
        } else {
            this.serviceName = serviceName;
        }
        if (serviceLibs == null) {
            this.serviceLibs = Collections.emptyList();
        } else {
            this.serviceLibs = serviceLibs;
        }
    }

    @Override
    public String getServiceName() {
        return this.serviceName;
    }

    @Override
    public List<String> getServiceLibs() {
        return Collections.unmodifiableList(this.serviceLibs);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultServiceInfo{");
        sb.append("serviceName='").append(serviceName).append('\'');
        sb.append(", serviceLibs=").append(serviceLibs).append("}");
        return sb.toString();
    }

}
