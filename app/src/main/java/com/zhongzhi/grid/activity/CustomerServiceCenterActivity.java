package com.zhongzhi.grid.activity;

import android.content.Intent;
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

public class CustomerServiceCenterActivity extends BaseActivity{
    private EditText content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_center);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        loadingDialog = createLoadingDialog(this,"");
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.elect);
        title.setText("客服中心");
        TextView history_feedback = (TextView)findViewById(R.id.history_feedback);
        history_feedback.setOnClickListener(this);
        content = (EditText)findViewById(R.id.content);
        Button conmmint = (Button)findViewById(R.id.conmmit);
        conmmint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.history_feedback:
                Intent intent = new Intent();
                intent.setClass(CustomerServiceCenterActivity.this,HistoryFeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.conmmit:
                String contentStr = content.getText().toString().trim();
                if(TextUtils.isEmpty(contentStr)){
                    showToast("请输入反馈内容");
                }else {
                    getData(contentStr);
                }

                break;
        }
    }

    /**
     * 获取数据
     */
    private void getData(String content) {
        if (!HttpUtil.isNetworkConnected(this)) {
            showToast(getString(R.string.net_hint));
            return;
        }
        loadingDialog.show();
        JSONObject ob = new JSONObject();
        try {
            ob.put("organizcode", SharedPrefsUtil.getValue(this,"organizCode",null));
            ob.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.ADD), new Callback.CommonCallback<String>() {
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
