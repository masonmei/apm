package com.baidu.oped.apm.mvc.vo;

/**
 * ***** description *****
 * Created with IntelliJ IDEA.
 * User: yangbolin
 * Date: 15/8/27
 * Time: 22:10
 * To change this template use File | Settings | File Templates.
 */
public class Page {

    private int pageNumber;
    private int pageSize;
    private long total;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
