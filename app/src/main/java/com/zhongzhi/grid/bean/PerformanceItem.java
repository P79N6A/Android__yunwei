package com.zhongzhi.grid.bean;

/**
 * Created by Administrator on 2018/7/12.
 */

public class PerformanceItem {
    private String DAY;
    private String MONTH;
    private String YEAR;
    private String USER_CODE;
    private String WORK_LIST_COUNT;
    private String DAY_SCORE;
    private String MONTH_SCORE;
    private String YEAR_SCORE;

    private String count;
    private String score;
    private String showTime;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getYEAR_SCORE() {
        return YEAR_SCORE;
    }

    public void setYEAR_SCORE(String YEAR_SCORE) {
        this.YEAR_SCORE = YEAR_SCORE;
    }

    public String getMONTH_SCORE() {
        return MONTH_SCORE;
    }

    public void setMONTH_SCORE(String MONTH_SCORE) {
        this.MONTH_SCORE = MONTH_SCORE;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public String getDAY() {
        return DAY;
    }

    public void setDAY(String DAY) {
        this.DAY = DAY;
    }

    public String getUSER_CODE() {
        return USER_CODE;
    }

    public void setUSER_CODE(String USER_CODE) {
        this.USER_CODE = USER_CODE;
    }

    public String getWORK_LIST_COUNT() {
        return WORK_LIST_COUNT;
    }

    public void setWORK_LIST_COUNT(String WORK_LIST_COUNT) {
        this.WORK_LIST_COUNT = WORK_LIST_COUNT;
    }

    public String getDAY_SCORE() {
        return DAY_SCORE;
    }

    public void setDAY_SCORE(String DAY_SCORE) {
        this.DAY_SCORE = DAY_SCORE;
    }
}
