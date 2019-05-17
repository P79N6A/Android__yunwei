package com.zhongzhi.grid.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongzhi.grid.R;
import com.zhongzhi.grid.bean.BaseBean;
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

public class ModifyPasswordActivity extends BaseActivity {
    private EditText mOldPassword;
    private EditText mNewPassword;
    private Button mCommit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        loadingDialog = createLoadingDialog(this,"");
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.elect);
        title.setText("修改密码");
        mOldPassword = (EditText)findViewById(R.id.old_password);
        mNewPassword = (EditText)findViewById(R.id.new_password);
        mCommit = (Button)findViewById(R.id.commit);
        mCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.commit:
                String old = mOldPassword.getText().toString().trim();
                String newPwd = mNewPassword.getText().toString().trim();
                if(TextUtils.isEmpty(old)){
                    showToast("请输入原密码");
                }else if(TextUtils.isEmpty(newPwd)){
                  showToast("请输入新密码");
                }else {
                    getData(old,newPwd);
                }
                break;
        }
    }

    /**
     * 修改密码
     */
    private void getData(String old,String newPwd) {
        if (!HttpUtil.isNetworkConnected(this)) {
            showToast(getString(R.string.net_hint));
            return;
        }
        loadingDialog.show();
        JSONObject ob = new JSONObject();
        try {
            ob.put("organizcode", SharedPrefsUtil.getValue(this,"organizCode",null));
            ob.put("oldpwd", old);
            ob.put("newpwd", newPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.MODIFY_PWD), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                loadingDialog.dismiss();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    showToast(bean.getMsg());
                    if (bean.getRet() == 1) {
                        finish();
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
}
