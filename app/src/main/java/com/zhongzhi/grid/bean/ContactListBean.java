package com.zhongzhi.grid.bean;

import com.zhongzhi.grid.fragment.ContactUserBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/12.
 */

public class ContactListBean {
    private ContactUserBean pageData;
    private ArrayList<ContactItem> departList;

    public ContactUserBean getPageData() {
        return pageData;
    }

    public void setPageData(ContactUserBean pageData) {
        this.pageData = pageData;
    }

    public ArrayList<ContactItem> getDepartList() {
        return departList;
    }

    public void setDepartList(ArrayList<ContactItem> departList) {
        this.departList = departList;
    }
}
