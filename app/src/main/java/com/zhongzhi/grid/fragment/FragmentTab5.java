package com.zhongzhi.grid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.activity.CustomerServiceCenterActivity;
import com.zhongzhi.grid.activity.ExperienceIndustryActivity;
import com.zhongzhi.grid.activity.LoginActivity;
import com.zhongzhi.grid.activity.SettingActivity;
import com.zhongzhi.grid.activity.UserInfoActivity;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.RepairOrderListBean;
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
 * Created by Administrator on 2018/7/7.
 */

public class FragmentTab5 extends BaseFragment {
    private TextView name;
    private TextView org;
    private TextView userCode;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_5,container,false);
        TextView edit_userinfo = (TextView)view.findViewById(R.id.edit_userinfo);
        TextView customer_service_center = (TextView)view.findViewById(R.id.customer_service_center);
        TextView setting = (TextView)view.findViewById(R.id.setting);
        TextView experience_industry = (TextView)view.findViewById(R.id.experience_industry);
        TextView logout = (TextView)view.findViewById(R.id.logout);
          name = (TextView)view.findViewById(R.id.name);
          org = (TextView)view.findViewById(R.id.org);
          userCode = (TextView)view.findViewById(R.id.user_num);
        setting.setOnClickListener(this);
        edit_userinfo.setOnClickListener(this);
        customer_service_center.setOnClickListener(this);
        experience_industry.setOnClickListener(this);
        logout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.edit_userinfo:
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.customer_service_center://客服中心
                Intent intent2 = new Intent(getContext(), CustomerServiceCenterActivity.class);
                startActivity(intent2);
                break;
            case R.id.setting:
                Intent intent3 = new Intent(getContext(), SettingActivity.class);
                startActivity(intent3);
                break;
            case R.id.experience_industry:
                Intent intent4 = new Intent(getContext(), ExperienceIndustryActivity.class);
                startActivity(intent4);
                break;
            case R.id.logout:
                SharedPrefsUtil.putValue(getContext(),"organizCode",null);
                Intent intent5 = new Intent(getContext(), LoginActivity.class);
                startActivity(intent5);
                getActivity().finish();
                break;
        }
    }


    /**
     * 获取数据
     */
    private void getData() {
        if (!HttpUtil.isNetworkConnected(getContext())) {
            showToast(getString(R.string.net_hint));
            return;
        }

        JSONObject ob = new JSONObject();
        try {
//            ob.put("username", SharedPrefsUtil.getValue(getContext(),"userName",null));
            ob.put("organizcode", SharedPrefsUtil.getValue(getContext(),"organizCode",null));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), getContext(), Constants.MY_INDEX), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);

                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        UserInfoBean userInfoBean = new Gson().fromJson(bean.getData(),UserInfoBean.class);
                        name.setText(userInfoBean.getNAME());
                        org.setText("单位名称："+userInfoBean.getDEPARTMENT());
                        userCode.setText("员工编号："+userInfoBean.getORGANIZ_CODE());

                    } else {
                        showToast(bean.getMsg());

                    }

                } else {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }
}
