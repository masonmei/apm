
package com.baidu.oped.apm.profiler.modifier.orm.ibatis.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * class SqlMapClientMethodFilter 
 *
 * @author meidongxu@baidu.com
 */
public class SqlMapClientMethodFilter extends IbatisMethodFilter {

    private static final Set<String> WHITE_LIST_API = createWhiteListApi();

    private static Set<String> createWhiteListApi() {
        return new HashSet<String>(Arrays.asList(IbatisInterfaceApi.sqlMapClientApis));
    }

    protected final boolean shouldTrackMethod(String methodName) {
        return WHITE_LIST_API.contains(methodName);
    }
}
