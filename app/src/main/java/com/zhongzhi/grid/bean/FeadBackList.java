package com.zhongzhi.grid.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/25.
 */

public class FeadBackList {
    private int page;
    private int pageSize;
    private int total;
    private ArrayList<FeadBackItem> results;

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

    public ArrayList<FeadBackItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<FeadBackItem> results) {
        this.results = results;
    }
}
