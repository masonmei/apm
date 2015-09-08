package com.baidu.oped.apm.mvc.vo;

/**
 * Created by mason on 9/7/15.
 */
public class PageInfo {
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
