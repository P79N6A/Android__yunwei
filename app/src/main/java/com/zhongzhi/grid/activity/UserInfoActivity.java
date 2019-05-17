package com.zhongzhi.grid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.UserInfoBean;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.HttpUtil;
import com.zhongzhi.grid.util.SharedPrefsUtil;
import com.zhongzhi.grid.util.Tool;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import static com.zhongzhi.grid.GridAplication.getContext;
import static com.zhongzhi.grid.util.ToastUtils.showToast;

/**
 * Created by Administrator on 2018/7/9.
 */

public class UserInfoActivity extends BaseActivity {
    private TextView name;
    private TextView code;
    private TextView org;
    private TextView phone;
    private TextView phone1;
    private TextView idNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        initView();
        getData();
    }

    @Override
    protected void initView() {
        super.initView();
        loadingDialog = createLoadingDialog(this,"");
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.elect);
         name = (TextView)findViewById(R.id.name);
         code = (TextView)findViewById(R.id.code);
         org = (TextView)findViewById(R.id.org);
         phone = (TextView)findViewById(R.id.phone);
         phone1 = (TextView)findViewById(R.id.phone2);
         idNum = (TextView)findViewById(R.id.id_num);
        title.setText("个人资料");
        RelativeLayout contactLayout1 = (RelativeLayout)findViewById(R.id.contact_layout1);
        RelativeLayout contactLayout2 = (RelativeLayout)findViewById(R.id.contact_layout2);
        RelativeLayout idNumLayout = (RelativeLayout)findViewById(R.id.id_num_layout);
        contactLayout1.setOnClickListener(this);
        contactLayout2.setOnClickListener(this);
        idNumLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back:
               finish();
                break;
            case R.id.contact_layout1:
                Intent intent = new Intent();
                intent.putExtra("type",1);
                intent.setClass(UserInfoActivity.this,EditUserInfoActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.contact_layout2:
                Intent intent2 = new Intent();
                intent2.putExtra("type",2);
                intent2.setClass(UserInfoActivity.this,EditUserInfoActivity.class);
                startActivityForResult(intent2,2);
                break;
            case R.id.id_num_layout:
                Intent intent3 = new Intent();
                intent3.putExtra("type",3);
                intent3.setClass(UserInfoActivity.this,EditUserInfoActivity.class);
                startActivityForResult(intent3,3);
                break;
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        if (!HttpUtil.isNetworkConnected(this)) {
            showToast(getString(R.string.net_hint));
            return;
        }

        JSONObject ob = new JSONObject();
        try {
//            ob.put("username", SharedPrefsUtil.getValue(this,"userName",null));
            ob.put("organizcode", SharedPrefsUtil.getValue(getContext(),"organizCode",null));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.MY_DETAIL), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                 loadingDialog.show();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        UserInfoBean userInfoBean = new Gson().fromJson(bean.getData(),UserInfoBean.class);
                        name.setText(userInfoBean.getNAME());
                        code.setText(userInfoBean.getORGANIZ_CODE());
                        org.setText(userInfoBean.getDEPARTMENT());
                        if(!TextUtils.isEmpty(userInfoBean.getPHONE_NUM1())){
                            phone.setText(userInfoBean.getPHONE_NUM1());
                        }
                        if(!TextUtils.isEmpty(userInfoBean.getPHONE_NUM2())){
                            phone1.setText(userInfoBean.getPHONE_NUM2());
                        }
                        if(!TextUtils.isEmpty(userInfoBean.getIDC_NUMBER())){
                            idNum.setText(userInfoBean.getIDC_NUMBER());
                        }

                    } else {
                        showToast(bean.getMsg());

                    }

                } else {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                loadingDialog.dismiss();
            }

            @Override
            public void onFinished() {
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1){
            phone.setText(data.getExtras().getString("val"));
        }
        if (requestCode == 2 && resultCode == 1){
            phone1.setText(data.getExtras().getString("val"));
        }
        if (requestCode == 3 && resultCode == 1){
            idNum.setText(data.getExtras().getString("val"));
        }
    }
}
