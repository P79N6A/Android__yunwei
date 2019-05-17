package com.zhongzhi.grid.fragment;

import com.zhongzhi.grid.bean.ContactItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/12.
 */

public class ContactUserBean {
    private int page;
    private int pageSize;
    private int total;
    private int start;
    private int pageTotal;
    private int end;
    private ArrayList<ContactItem> results;

    public ArrayList<ContactItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<ContactItem> results) {
        this.results = results;
    }

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
}
