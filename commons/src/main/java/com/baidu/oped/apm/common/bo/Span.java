
package com.baidu.oped.apm.common.bo;

import java.util.List;

import com.baidu.oped.apm.common.ServiceType;

/**
 * class Span 
 *
 * @author meidongxu@baidu.com
 */
public interface Span {
    ServiceType getServiceType();

    String getRpc();

    String getEndPoint();

    List<AnnotationBo> getAnnotationBoList();
}
