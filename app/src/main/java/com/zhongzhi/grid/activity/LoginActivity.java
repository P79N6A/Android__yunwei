package com.zhongzhi.grid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.SelectUserInfoBean;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.HttpUtil;
import com.zhongzhi.grid.util.SharedPrefsUtil;
import com.zhongzhi.grid.util.Tool;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import static com.zhongzhi.grid.util.ToastUtils.showToast;

/**
 * Created by Administrator on 2018/7/9.
 */

    public class LoginActivity extends BaseActivity {

    private EditText mUserName;
    private EditText mPassword;
    private TextView user_name_tv;
    private TextView org_tv;
    private TextView org_num;
    private String organizCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                      String str = charSequence.toString();
                      if(str.length() == 11 ){
                          login(str);
                      }
//                      else if(str.length() == 12){
//                          login(str);
//                      }
                      else{
                          user_name_tv.setVisibility(View.GONE);
                          org_tv.setVisibility(View.GONE);
                          org_num.setVisibility(View.GONE);
                      }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        loadingDialog = createLoadingDialog(this,"");
        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(this);
        mUserName = (EditText)findViewById(R.id.user_name);
        mPassword = (EditText)findViewById(R.id.password);
        user_name_tv = (TextView)findViewById(R.id.user_name_tv);
        org_tv = (TextView)findViewById(R.id.org_tv);
        org_num = (TextView)findViewById(R.id.org_num);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.login:
                String userName = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String org_Num = org_num.getText().toString().trim();
                String organizCode = org_Num.substring(3);
                if(TextUtils.isEmpty(userName)){
                   showToast(LoginActivity.this,"请输入用户名");
                }else if(TextUtils.isEmpty(password)){
                    showToast(LoginActivity.this,"请输入密码");
                }else {
                    login(userName,password,organizCode);
                }
                break;
        }
    }

    /**
     * 登录
     * @param userName 用户名
     * @param password 密码
     */
    private void login(final String userName, String password, final String organizCode) {
        if (!HttpUtil.isNetworkConnected(this)) {
            showToast(getString(R.string.net_hint));
            return;
        }
        if(loadingDialog!=null && !loadingDialog.isShowing()){
            loadingDialog.show();
        }

        JSONObject ob = new JSONObject();
        try {
            ob.put("username",userName);
            ob.put("pwd", password);
            ob.put("organizcode",organizCode);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.LOGIN), new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                if(loadingDialog!=null){
                    loadingDialog.dismiss();
                }
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        SharedPrefsUtil.putValue(LoginActivity.this,"userName",userName.toUpperCase());
                        SharedPrefsUtil.putValue(LoginActivity.this,"organizCode",organizCode.toUpperCase());

                        String username = SharedPrefsUtil.getValue(LoginActivity.this,"userName",null);
                        String organizcode = SharedPrefsUtil.getValue(LoginActivity.this,"organizCode",null);
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast(bean.getMsg());

                    }

                } else {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                if(loadingDialog!=null){
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if(loadingDialog!=null){
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFinished() {
                if(loadingDialog!=null){
                    loadingDialog.dismiss();
                }
            }
        });
    }

    /**
     * 登录
     * @param userName 用户名
     */
    private void login(final String userName) {
        if (!HttpUtil.isNetworkConnected(this)) {
            showToast(getString(R.string.net_hint));
            return;
        }
        if(loadingDialog!=null && !loadingDialog.isShowing()){
            loadingDialog.show();
        }

        JSONObject ob = new JSONObject();
        try {
            ob.put("username",userName);
//            ob.put("organizcode",organizCode);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.SELECTUSERINFO), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                if(loadingDialog!=null){
                    loadingDialog.dismiss();
                }
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        SelectUserInfoBean selectUserInfoBean = new Gson().fromJson(bean.getData(),SelectUserInfoBean.class);
                        user_name_tv.setText("姓名："+selectUserInfoBean.getNAME());
                        org_tv.setText("单位："+selectUserInfoBean.getDEPARTMENT());
                        org_num.setText("工号："+selectUserInfoBean.getORGANIZ_CODE());
                        user_name_tv.setVisibility(View.VISIBLE);
                        org_tv.setVisibility(View.VISIBLE);
                        org_num.setVisibility(View.VISIBLE);
                    } else {
                        showToast(bean.getMsg());

                    }

                } else {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                if(loadingDialog!=null){
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if(loadingDialog!=null){
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFinished() {
                if(loadingDialog!=null){
                    loadingDialog.dismiss();
                }
            }
        });
    }

}
