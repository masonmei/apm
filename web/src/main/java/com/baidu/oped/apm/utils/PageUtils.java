package com.baidu.oped.apm.utils;

import com.sun.tools.javac.util.Pair;
import org.springframework.util.StringUtils;

/**
 * 分页工具类
 * Created with IntelliJ IDEA.
 * User: yangbolin
 * Date: 15/8/27
 * Time: 21:20
 * To change this template use File | Settings | File Templates.
 */
public class PageUtils {

    public static Pair<Boolean, String> parseOrder(String orderBy) {
        if (StringUtils.isEmpty(orderBy)) {
            return null;
        }
        boolean order = true;
        if (orderBy.startsWith("-")) {
            order = false;
        }
        Pair<Boolean, String> pair = new Pair<>(order, orderBy.substring(1));
        return pair;
    }

}
