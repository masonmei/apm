
package com.baidu.oped.apm.profiler.modifier.orm.mybatis.interceptor;

import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.profiler.modifier.orm.SqlMapOperationInterceptor;

/**
 * class MyBatisSqlMapOperationInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class MyBatisSqlMapOperationInterceptor extends SqlMapOperationInterceptor {

    public MyBatisSqlMapOperationInterceptor(ServiceType serviceType) {
        super(serviceType, MyBatisSqlMapOperationInterceptor.class);
    }

}
