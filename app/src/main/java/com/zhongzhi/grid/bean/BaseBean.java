package com.zhongzhi.grid.bean;/**
 * Created by Administrator on 2017/6/22.
 */

import org.json.JSONException;
import org.json.JSONObject;

/**
 * created by lywh1987 at 2017/6/22
 */
public class BaseBean {
    private int ret;
    private String data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static BaseBean praseJSONObject(String string){
        BaseBean bean = new BaseBean();
        try {
            JSONObject object = new JSONObject(string);
            if (object.has("ret")){
                bean.setRet(object.getInt("ret"));
            }
            if (object.has("result"))
                bean.setRet(object.getInt("result"));
            if (object.has("data")){
                bean.setData(object.getString("data"));
            }
            if (object.has("msg")){
                bean.setMsg(object.getString("msg"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            bean = null;
        }
        return bean;
    }
}
