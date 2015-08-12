
package com.baidu.oped.apm.profiler.modifier.orm.ibatis.filter;

/**
 * class IbatisInterfaceApi 
 *
 * @author meidongxu@baidu.com
 */
public final class IbatisInterfaceApi {

    private IbatisInterfaceApi() {}

    private static final String[] sqlMapExecutorApis = {
        "insert",
        "delete",
        "update",
        "queryForList",
        "queryForMap",
        "queryForObject",
        "queryForPaginatedList",
        "queryForRowHandler"
    };
//    private static final String[] sqlMapTransactionManagerApis = {
//        "startTransaction",
//        "endTransaction",
//        "commitTransaction",
//        "getDataSource",
//        "getUserConnection",
//        "getCurrentConnection",
//    };
//    static final String[] sqlMapSessionApis = ArrayUtils.addAll(
//        ArrayUtils.addAll(sqlMapExecutorApis, sqlMapTransactionManagerApis),
//        "close"
//    );
    static final String[] sqlMapSessionApis = sqlMapExecutorApis;
//    static final String[] sqlMapClientApis = ArrayUtils.addAll(
//        sqlMapExecutorApis,
//        sqlMapTransactionManagerApis
//    );
    static final String[] sqlMapClientApis = sqlMapExecutorApis;

}
