package com.zhongzhi.grid.bean;

/**
 * Created by Administrator on 2018/7/13.
 */

public class UserInfoBean {
//    {"ret":1,"msg":"获取我的成功！","data":{"NAME":"张三","DEPARTMENT":"123","ORGANIZ_CODE":"JGGW1112111311"}}
    private String NAME;
    private String DEPARTMENT;
    private String ORGANIZ_CODE;
    private String PHONE_NUM1;
    private String PHONE_NUM2;
    private String IDC_NUMBER;

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

    public String getIDC_NUMBER() {
        return IDC_NUMBER;
    }

    public void setIDC_NUMBER(String IDC_NUMBER) {
        this.IDC_NUMBER = IDC_NUMBER;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public void setDEPARTMENT(String DEPARTMENT) {
        this.DEPARTMENT = DEPARTMENT;
    }

    public String getORGANIZ_CODE() {
        return ORGANIZ_CODE;
    }

    public void setORGANIZ_CODE(String ORGANIZ_CODE) {
        this.ORGANIZ_CODE = ORGANIZ_CODE;
    }
}
