package com.zhongzhi.grid.util;

import android.os.Environment;

/**
 * Created by Administrator on 2017/6/13.
 */

public  class Constants {
    /**是否打印LOG*/
    public static final boolean ISLOG = true;


    public static final int SURE = 1001;
    public static final int CANCEL = 1002;
    public static final int REQUEST_SUCCESS = 1003;
    public static final int REQUEST_FAIL = 1004;
    public static final int REQUEST_FINISH = 1005;
    public static final String IS_FIRST_IN = "isfirstin";//程序首次安装
    public static String SHARED_PREFERENCE_NAME="grid";
    public static  final java.lang.String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static java.lang.String BASE_PATH = SDCARD_PATH + "/Grid";
//     public static final String BASE_URL = "http://192.168.50.142:8080/grid";
   /* public static final String BASE_URL = "http://192.168.1.50:8080/grid";
     public static final String IMG_URL = "http://192.168.1.50:8080/grid/";*/
   public static final String BASE_URL = "http://61.163.34.143:8088/grid";
   public static final String IMG_URL = "http://61.163.34.143:8088/grid/";
      /**
      * 登录
      * */
    public static final String LOGIN = BASE_URL + "/rest/logining/logining.cs";
    /**
     * 登录信息查询
     * */
    public static final String SELECTUSERINFO = BASE_URL + "/rest/logining/selectUserInfo.cs";
    /**
     * 我的
     * */
    public static final String MY_INDEX = BASE_URL + "/rest/my/myIndex.cs";
    /**
     * 个人资料详情
     * */
    public static final String MY_DETAIL= BASE_URL + "/rest/my/detail.cs";
    /**
     * 今日工单查询
     * */
    public static final String SELECT_ALL = BASE_URL + "/rest/today/selectAll.cs";
    /**
     * 今日工单查询供电单位
     * */
    public static final String SELECT_MANAGE = BASE_URL + "/rest/today/MANAGE_UNIT.cs";
    /**
     * 今日工单查询台区名称
     * */
    public static final String SELECT_AREA = BASE_URL + "/rest/today/AREA_NAME.cs";
    /**
     * 今日工单查询供电单位
     * */
    public static final String SELECT_EXPLAIN= BASE_URL + "/rest/today/DISPACH_EXPLAIN.cs";
    /**
     * 历史所有工单查询
     * */
    public static final String HISTORY_COUNTALL = BASE_URL + "/rest/history/selectAll.cs";
    /**
     * 历史未处理工单查询
     * */
    public static final String HISTORY_SELECT_COUNTUNPROCESSED = BASE_URL + "/rest/history/countUnprocessed.cs";
    /**
     * 历史已处理工单查询
     * */
    public static final String HISTORY_SELECT_COUNTPROCESSED = BASE_URL + "/rest/history/countProcessed.cs";
    /**
     * 业绩日统计
     * */
    public static final String DDETAILS = BASE_URL + "/rest/PerformanceD/ddetails.cs";
    /**
     * 业绩月统计
     * */
    public static final String NDETAILS = BASE_URL + "/rest/PerformanceM/mdetails.cs";
    /**
     * 业绩年统计
     * */
    public static final String YDETAILS = BASE_URL + "/rest/PerformanceY/ydetails.cs";
    /**
     * 行业经验
     * */
    public static final String EXPERIENCE_LIST = BASE_URL + "/rest/experience/list.cs";
    /**
     * 今日工单详情
     * */
    public static final String SELECTDETAIL = BASE_URL + "/rest/today/selectDetail.cs";
    /**
     * 历史工单详情
     * */
    public static final String HISTORY_SELECTDETAIL = BASE_URL + "/rest/history/historyDetail.cs";
    /**
     * 通讯录
     * */
    public static final String COMPANY_LIST = BASE_URL + "/rest/contacts/company_list.cs";
    /**
     * 今日工单下派
     * */
    public static final String CONFIRM = BASE_URL + "/rest/today/confirm.cs";
    /**
     * 处理订单
     * */
    public static final String ADDTODAY = BASE_URL + "/rest/today/addToday.cs";
    /**
     * 修改个人资料
     * */
    public static final String EDIT = BASE_URL + "/rest/my/edit.cs";
    /**
     * 添加反馈信息
     * */
    public static final String ADD = BASE_URL + "/rest/feedback/add.cs";
    /**
     * 反馈历史接口
     * */
    public static final String FEEDBACK_LIST = BASE_URL + "/rest/feedback/history.cs";
    /**
     * 修改密码
     * */
    public static final String MODIFY_PWD = BASE_URL + "/rest/my/updatePwd.cs";
    /**
     * 上传经纬度
     * */
    public static final String SAVE_COORDINATE = BASE_URL + "/rest/today/saveCoordinate.cs";
}
