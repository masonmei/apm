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
public class QueryResponse<T> {

    private Page page;
    private List<T> data;

    public QueryResponse(Page page, List<T> data) {
        this.page = page;
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
