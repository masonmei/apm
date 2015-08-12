
package com.baidu.oped.apm.profiler.modifier.orm.mybatis.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.baidu.oped.apm.bootstrap.instrument.MethodFilter;
import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;

/**
 * class SqlSessionMethodFilter 
 *
 * @author meidongxu@baidu.com
 */
public class SqlSessionMethodFilter implements MethodFilter {

    private static final boolean TRACK = false;
    private static final boolean DO_NOT_TRACK = true;

    private static final Set<String> WHITE_LIST_API = createWhiteListApi();

    private static Set<String> createWhiteListApi() {
        return new HashSet<String>(Arrays.asList(
                "selectOne",
                "selectList",
                "selectMap",
                "select",
                "insert",
                "update",
                "delete"
//                "commit",
//                "rollback",
//                "flushStatements",
//                "close",
//                "getConfiguration",
//                "getMapper",
//                "getConnection"
        ));
    }

    @Override
    public boolean filter(MethodInfo ctMethod) {
        if (WHITE_LIST_API.contains(ctMethod.getName())) {
            return TRACK;
        }
        return DO_NOT_TRACK;
    }

}
