package com.zhongzhi.grid.bean;

/**
 * Created by Administrator on 2018/7/25.
 */

public class FeadBackItem {
    private String ID;
    private String FEEDBACK_PERSON;
    private String FEEDBACK_DATE_TIME;
    private String FEEDBACK_CONTENT;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFEEDBACK_PERSON() {
        return FEEDBACK_PERSON;
    }

    public void setFEEDBACK_PERSON(String FEEDBACK_PERSON) {
        this.FEEDBACK_PERSON = FEEDBACK_PERSON;
    }

    public String getFEEDBACK_DATE_TIME() {
        return FEEDBACK_DATE_TIME;
    }

    public void setFEEDBACK_DATE_TIME(String FEEDBACK_DATE_TIME) {
        this.FEEDBACK_DATE_TIME = FEEDBACK_DATE_TIME;
    }

    public String getFEEDBACK_CONTENT() {
        return FEEDBACK_CONTENT;
    }

    public void setFEEDBACK_CONTENT(String FEEDBACK_CONTENT) {
        this.FEEDBACK_CONTENT = FEEDBACK_CONTENT;
    }
}
