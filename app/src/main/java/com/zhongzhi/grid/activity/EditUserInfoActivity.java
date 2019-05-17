package com.zhongzhi.grid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import static com.zhongzhi.grid.util.ToastUtils.showToast;

/**
 * Created by Administrator on 2018/7/9.
 */

public class EditUserInfoActivity extends BaseActivity {
    private int type;
    private TextView key;
    private EditText value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_userinfo);
        type = getIntent().getExtras().getInt("type");
        initView();
        switch (type){
            case 1:
                key.setText("联系方式");
                value.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case 2:
                key.setText("备用联系方式");
                value.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case 3:
                key.setText("身份证号");
//                value.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        loadingDialog = createLoadingDialog(this,"");
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        TextView save = (TextView)findViewById(R.id.save);
        save.setText("保存");
        save.setOnClickListener(this);
        key = (TextView)findViewById(R.id.key);
        value = (EditText)findViewById(R.id.value);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.save:
                String str = value.getText().toString().trim();
                if(!TextUtils.isEmpty(str)){
                    getData(str);
                }
                break;
        }
    }

    /**
     * 获取数据
     */
    private void getData(final String val) {
        if (!HttpUtil.isNetworkConnected(this)) {
            showToast(getString(R.string.net_hint));
            return;
        }
        loadingDialog.show();
        JSONObject ob = new JSONObject();
        try {
            ob.put("organizcode", SharedPrefsUtil.getValue(this,"organizCode",null));
            ob.put("paraVal",val);
            ob.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.EDIT), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                loadingDialog.dismiss();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        Intent intent = new Intent();
                        intent.putExtra("val",val);
                        setResult(1,intent);
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
