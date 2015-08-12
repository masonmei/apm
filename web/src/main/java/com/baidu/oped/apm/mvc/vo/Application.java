
package com.baidu.oped.apm.mvc.vo;

import com.baidu.oped.apm.common.ServiceType;

/**
 * class Application 
 *
 * @author meidongxu@baidu.com
 */
public final class Application {
    private final String name;
    private final ServiceType serviceType;
    // store separately to track undefined cases more easily
    private final short code;

    public Application(String name, ServiceType serviceType) {
        if (name == null) {
            throw new NullPointerException("name must not be null");
        }
        if (serviceType == null) {
            throw new NullPointerException("serviceType must not be null");
        }
        this.name = name;
        this.serviceType = serviceType;
        this.code = serviceType.getCode();
    }

    public Application(String name, short serviceType) {
        if (name == null) {
            throw new NullPointerException("name must not be null");
        }
        this.name = name;
        this.serviceType = ServiceType.findServiceType(serviceType);
        this.code = serviceType;
    }

    public String getName() {
        return name;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public short getServiceTypeCode() {
        return serviceType.getCode();
    }

    public short getCode() {
        return code;
    }

    public boolean equals(String thatName, ServiceType thatServiceType) {
        if (thatName == null) {
            throw new NullPointerException("thatName must not be null");
        }
        if (thatServiceType == null) {
            throw new NullPointerException("thatServiceType must not be null");
        }
        if (serviceType != thatServiceType) return false;
        if (!name.equals(thatName)) return false;

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Application that = (Application) o;

        if (serviceType != that.serviceType) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + serviceType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + "(" + serviceType + ":" + code + ")";
    }
}
