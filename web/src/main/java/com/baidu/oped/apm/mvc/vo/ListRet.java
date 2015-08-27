package com.baidu.oped.apm.mvc.vo;

import java.util.List;

/**
 * ***** description *****
 * Created with IntelliJ IDEA.
 * User: yangbolin
 * Date: 15/8/27
 * Time: 22:14
 * To change this template use File | Settings | File Templates.
 */
public class ListRet {

    private Page page;
    private List data;

    public ListRet(Page page, List data) {
        this.page = page;
        this.data = data;
    }
}
