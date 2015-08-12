
package com.baidu.oped.apm.profiler.monitor.metric;

import com.baidu.oped.apm.common.ServiceType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * class DynamicAcceptHistogram 
 *
 * @author meidongxu@baidu.com
 */
public class DynamicAcceptHistogram implements AcceptHistogram {

    private final ConcurrentMap<ResponseKey, Histogram> map;

    public DynamicAcceptHistogram() {
        this.map = new ConcurrentHashMap<ResponseKey, Histogram>();
    }

    @Override
    public boolean addResponseTime(String parentApplicationName, short serviceTypeCode, int millis) {
        if (parentApplicationName == null) {
            throw new NullPointerException("parentApplicationName must not be null");
        }
        // Cannot compare by ServiceType value because it could be incompatible if new service type is added.  
        if (!ServiceType.isWas(serviceTypeCode)) {
            return false;
        }
        
        // TODO As already explained, ServiceType.UNDEFINED is returned if serviceTypeCode is of new service type which is added to newer version.
        // How to handle this situation?
        // We can infer if we know the type of histogramSchema. Server can determine the server type with code + schemaType. 
        final ServiceType serviceType = ServiceType.findServiceType(serviceTypeCode);
        if (serviceType == ServiceType.UNDEFINED) {
            return false;
        }
        final ResponseKey responseKey = new ResponseKey(parentApplicationName, serviceType);
        final Histogram histogram = getHistogram(responseKey);
        histogram.addResponseTime(millis);
        return true;
    }

    private Histogram getHistogram(ResponseKey responseKey) {
        final Histogram hit = map.get(responseKey);
        if (hit != null) {
            return hit;
        }
        final Histogram histogram = new LongAdderHistogram(responseKey.getServiceType());
        final Histogram old = map.putIfAbsent(responseKey, histogram);
        if (old != null) {
            return old;
        }
        return histogram;
    }


    private static final class ResponseKey {
        private final ServiceType serviceType;
        private final String parentApplicationName;

        private ResponseKey(String parentApplicationName, ServiceType serviceType) {
            if (parentApplicationName == null) {
                throw new NullPointerException("parentApplicationName must not be null");
            }
            if (serviceType == null) {
                throw new NullPointerException("serviceType must not be null");
            }

            this.parentApplicationName = parentApplicationName;
            this.serviceType = serviceType;
        }

        public String getParentApplicationName() {
            return parentApplicationName;
        }

        public ServiceType getServiceType() {
            return serviceType;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ResponseKey that = (ResponseKey) o;

            if (!parentApplicationName.equals(that.parentApplicationName)) return false;
            if (serviceType != that.serviceType) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = serviceType.hashCode();
            result = 31 * result + parentApplicationName.hashCode();
            return result;
        }
    }
}
