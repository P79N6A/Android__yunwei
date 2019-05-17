package com.zhongzhi.grid.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/12.
 */

public class PerformanceListBean {
    private int page;
    private int pageSize;
    private int total;
    private int start;
    private int pageTotal;
    private int end;
    private ArrayList<PerformanceItem> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public ArrayList<PerformanceItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<PerformanceItem> results) {
        this.results = results;
    }
}
