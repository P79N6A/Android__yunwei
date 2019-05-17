package com.zhongzhi.grid.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/12.
 */

public class ContactItem implements Serializable{

    private String organiz_code;
    private String organiz_post_name;
    private String ORGANIZ_POST_NAME;
    private String TYPE;
    private String name;
    private String PHONE_NUM1;
    private String PHONE_NUM2;
    private String pagenum;
    private String ORGANIZ_CODE;

    public String getORGANIZ_CODE() {
        return ORGANIZ_CODE;
    }

    public void setORGANIZ_CODE(String ORGANIZ_CODE) {
        this.ORGANIZ_CODE = ORGANIZ_CODE;
    }

    public String getORGANIZ_POST_NAME() {
        return ORGANIZ_POST_NAME;
    }

    public void setORGANIZ_POST_NAME(String ORGANIZ_POST_NAME) {
        this.ORGANIZ_POST_NAME = ORGANIZ_POST_NAME;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPHONE_NUM1() {
        return PHONE_NUM1;
    }

    public void setPHONE_NUM1(String PHONE_NUM1) {
        this.PHONE_NUM1 = PHONE_NUM1;
    }

    public String getPHONE_NUM2() {
        return PHONE_NUM2;
    }

    public void setPHONE_NUM2(String PHONE_NUM2) {
        this.PHONE_NUM2 = PHONE_NUM2;
    }

    public String getPagenum() {
        return pagenum;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }

    public String getOrganiz_code() {
        return organiz_code;
    }

    public void setOrganiz_code(String organiz_code) {
        this.organiz_code = organiz_code;
    }

    public String getOrganiz_post_name() {
        return organiz_post_name;
    }

    public void setOrganiz_post_name(String organiz_post_name) {
        this.organiz_post_name = organiz_post_name;
    }
}
