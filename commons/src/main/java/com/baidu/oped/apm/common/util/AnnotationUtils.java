
package com.baidu.oped.apm.common.util;

import java.util.*;

import com.baidu.oped.apm.common.AnnotationKey;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.common.bo.AnnotationBo;
import com.baidu.oped.apm.common.bo.Span;

/**
 * class AnnotationUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class AnnotationUtils {
    private AnnotationUtils() {
    }

    public static String findApiAnnotation(List<AnnotationBo> list) {
        if (list == null) {
            return null;
        }
        AnnotationBo annotationBo = findAnnotationBo(list, AnnotationKey.API);
        if (annotationBo != null) {
            return (String) annotationBo.getValue();
        }
        return null;
    }

    public static AnnotationBo findAnnotationBo(List<AnnotationBo> annotationBoList, AnnotationKey annotationKey) {
        for (AnnotationBo annotation : annotationBoList) {
            int key = annotation.getKey();
            if (annotationKey.getCode() == key) {
                return annotation;
            }
        }
        return null;
    }

    public static AnnotationBo findArgsAnnotationBo(List<AnnotationBo> annotationBoList) {
        for (AnnotationBo annotation : annotationBoList) {
            if (AnnotationKey.isArgsKey(annotation.getKey())) {
                return annotation;
            }
        }
        return null;
    }

    public static AnnotationBo getDisplayArgument(Span span) {
        // TODO needs a more generalized implementation for Arcus
        List<AnnotationBo> list = span.getAnnotationBoList();
        if (list == null) {
            return null;
        }
        final ServiceType serviceType = span.getServiceType();
        if (serviceType == ServiceType.ARCUS || serviceType == ServiceType.MEMCACHED) {
            // Displays any value within the first argument.
            // TODO Values may be missing when there are 2+ parameters - since there is only 1 parameter dump key for Arcus, it migth be okay for now.
            return findArgsAnnotationBo(list);
        }

        // TODO needs a more generalized implementation for rpc connectors
        if (serviceType == ServiceType.HTTP_CLIENT || serviceType == ServiceType.JDK_HTTPURLCONNECTOR) {
            return findAnnotationBo(list, AnnotationKey.HTTP_URL);
        }
        
        if (serviceType == ServiceType.HTTP_CLIENT_INTERNAL) {
            return findAnnotationBo(list, AnnotationKey.HTTP_CALL_RETRY_COUNT);
        }
        
        if (serviceType == ServiceType.BLOC_INTERNAL_METHOD) {
            return findAnnotationBo(list, AnnotationKey.CAll_URL);
        }

//        For Tomcat spans, there is no need to lookup using annotation as they can just use the rpc field within the Span
//        if (span.getServiceType() == ServiceType.TOMCAT) {
//            return findAnnotationBo(list, AnnotationKey.HTTP_URL);
//        }
//
        // TODO something needs fixing
        if (serviceType == ServiceType.MYSQL || serviceType == ServiceType.MYSQL_EXECUTE_QUERY
                || serviceType == ServiceType.ORACLE || serviceType == ServiceType.ORACLE_EXECUTE_QUERY
                || serviceType == ServiceType.MSSQL || serviceType == ServiceType.MSSQL_EXECUTE_QUERY
                || serviceType == ServiceType.CUBRID || serviceType == ServiceType.CUBRID_EXECUTE_QUERY) {
            // args0 is a string
            // TODO needs better implementation
            return findAnnotationBo(list, AnnotationKey.ARGS0);
        }
        
        if (serviceType == ServiceType.IBATIS || serviceType == ServiceType.MYBATIS) {
            return findAnnotationBo(list, AnnotationKey.ARGS0);
        }
        
        if (serviceType == ServiceType.SPRING_ORM_IBATIS) {
            return findAnnotationBo(list, AnnotationKey.ARGS0);
        }
        
        return null;
    }

    private static  List<AnnotationKey> API_META_DATA_ERROR;
    static {
        API_META_DATA_ERROR = loadApiMetaDataError();
    }

    static List<AnnotationKey> loadApiMetaDataError() {
        List<AnnotationKey> apiMetaData = new ArrayList<AnnotationKey>();
        for (AnnotationKey annotationKey : AnnotationKey.values()) {
            if (annotationKey.name().startsWith("ERROR_API_METADATA_")) {
                apiMetaData.add(annotationKey);
            }
        }
         return apiMetaData;
    }

    public static AnnotationKey getApiMetaDataError(List<AnnotationBo> annotationBoList) {
        for (AnnotationBo bo : annotationBoList) {
            AnnotationKey apiErrorCode = findApiErrorCode(bo);
            if (apiErrorCode != null) {
                return apiErrorCode;
            }
        }
        // could not find a more specific error - returns generalized error
        return AnnotationKey.ERROR_API_METADATA_ERROR;
    }

    private static AnnotationKey findApiErrorCode(AnnotationBo bo) {
        for (AnnotationKey annotationKey : API_META_DATA_ERROR) {
            if (bo.getKey() == annotationKey.getCode()) {
                return annotationKey;
            }
        }
        return null;
    }

}
