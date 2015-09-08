package com.baidu.oped.apm.utils;

import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

/**
 * 分页工具类
 * Created with IntelliJ IDEA.
 * User: yangbolin
 * Date: 15/8/27
 * Time: 21:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class PageUtils {

    public static Sort toSort(String orderBy) {
        if (StringUtils.isEmpty(orderBy)) {
            return null;
        }

        if (orderBy.startsWith("-")) {
            return new Sort(Sort.Direction.DESC, orderBy.substring(1));
        }

        if (!orderBy.startsWith("+")) {
            orderBy = "+" + orderBy;
        }

        return new Sort(Sort.Direction.ASC, orderBy.substring(1));

    }

}
