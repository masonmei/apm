
package com.baidu.oped.apm.profiler.modifier.orm.ibatis.interceptor;

import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.profiler.modifier.orm.SqlMapOperationInterceptor;

/**
 * class IbatisSqlMapOperationInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class IbatisSqlMapOperationInterceptor extends SqlMapOperationInterceptor {

    public IbatisSqlMapOperationInterceptor(ServiceType serviceType) {
        super(serviceType, IbatisSqlMapOperationInterceptor.class);
    }

}
