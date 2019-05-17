package com.zhongzhi.grid.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.RepairOrderInfoBean;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.HttpUtil;
import com.zhongzhi.grid.util.SharedPrefsUtil;
import com.zhongzhi.grid.util.Tool;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import static com.zhongzhi.grid.GridAplication.getContext;
import static com.zhongzhi.grid.util.Constants.IMG_URL;
import static com.zhongzhi.grid.util.ToastUtils.showToast;

/**
 * Created by Administrator on 2018/7/9.
 */

public class  HistoryRepairOrderInfoActivity extends BaseActivity {


    private TextView location_addr;
    private String worknum;
    private TextView orderNum;
    private TextView userNum;
    private TextView ziChanNum;
    private TextView userName;
    private TextView biaoxiangNum;
    private TextView taiquNum;
    private TextView taiquName;
    private TextView startTime;
    private TextView endTime;
    private TextView paidanren;
    private TextView gdScore;
    private TextView myScore;
    private LinearLayout edit_layout;
    private Button conmmit;
    private RadioGroup radioGroup;
    private TextView pai_dan_shuoming;
    private TextView chuli_banfa;
    private ImageView img;
    private EditText mUserName;
    private String org_num;
    private String FLAG_SUCCESS;
//    private PhotoAdapter photoAdapter;

    private  String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_repair_order_info);
//        setContentView(R.layout.activity_login);
//        mUserName = getIntent().getExtras().getString("mUserName");
        org_num = SharedPrefsUtil.getValue(this,"org_num",null);
        worknum = getIntent().getExtras().getString("worknum");
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
        initView();
        getData();
    }

    @Override
    protected void initView() {
        super.initView();
        loadingDialog = createLoadingDialog(this,"");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.elect);
        orderNum = (TextView) findViewById(R.id.order_num);
        ziChanNum = (TextView)findViewById(R.id.zi_chan_num);
        userNum = findViewById(R.id.user_num);
        userName = (TextView)findViewById(R.id.user_name);
        biaoxiangNum = (TextView)findViewById(R.id.biao_xiang_num);
        taiquNum = (TextView)findViewById(R.id.tai_qu_num);
        taiquName = (TextView)findViewById(R.id.tai_qu_name);
        startTime = (TextView)findViewById(R.id.start_time);
        endTime = (TextView)findViewById(R.id.end_time);
        paidanren = (TextView)findViewById(R.id.pai_dan_ren);
        gdScore = (TextView)findViewById(R.id.gd_score);
        myScore = (TextView)findViewById(R.id.my_score);
        pai_dan_shuoming = (TextView) findViewById(R.id.pai_dan_shuoming);
        chuli_banfa = (TextView)findViewById(R.id.chuli_banfa);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        edit_layout = (LinearLayout)findViewById(R.id.edit_layout);
        img = (ImageView)findViewById(R.id.img);
        img.setOnClickListener(this);


//        if(isEdit){
//            conmmit.setVisibility(View.VISIBLE);
//            img.setEnabled(true);
//        }else {
//            conmmit.setVisibility(View.GONE);
//            enableRadioGroup(radioGroup);
//            radioGroup.check(R.id.radio2);
//            img.setEnabled(false);
//        }
        title.setText("工单详情");
        location_addr = (TextView)findViewById(R.id.location_addr);

    }




    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img:
                Intent intent = new Intent(HistoryRepairOrderInfoActivity.this, BigImageActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("imgURL",imgPath);
                intent.putExtras(bundle);
                startActivity(intent);

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
        loadingDialog.show();
        JSONObject ob = new JSONObject();
        try {
            ob.put("worknum", worknum);
            ob.put("organizcode", SharedPrefsUtil.getValue(getContext(),"organizCode",null));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.HISTORY_SELECTDETAIL), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                loadingDialog.dismiss();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        RepairOrderInfoBean repairOrderInfoBean = new Gson().fromJson(bean.getData(),RepairOrderInfoBean.class);
                        orderNum.setText(repairOrderInfoBean.getWORK_LIST_NUM());
                        ziChanNum.setText(repairOrderInfoBean.getASSET_NUM());
                        userNum.setText(repairOrderInfoBean.getUSER_NUM());
                        userName.setText(repairOrderInfoBean.getELEC_USER_NAME());
                        biaoxiangNum.setText(repairOrderInfoBean.getMEASURE_BOX_ASSET_NUM());
                        taiquNum.setText(repairOrderInfoBean.getAREA_NUM());
                        taiquName.setText(repairOrderInfoBean.getAREA_NAME());
                        startTime.setText(repairOrderInfoBean.getBEGIN_DATE_TIME());
                        endTime.setText(repairOrderInfoBean.getEND_DATE_TIME());
                        paidanren.setText(repairOrderInfoBean.getDISPATCHER());
                        pai_dan_shuoming.setText(repairOrderInfoBean.getDISPACH_EXPLAIN());
                        gdScore.setText(repairOrderInfoBean.getLIST_SCORE());
                        myScore.setText(repairOrderInfoBean.getSCORE());
                        chuli_banfa.setText(repairOrderInfoBean.getOPERAT_METHOD());
                        FLAG_SUCCESS = repairOrderInfoBean.getFLAG_SUCCESS();
//                        Glide.with(HistoryRepairOrderInfoActivity.this).load(BASE_URL+repairOrderInfoBean.getPIC_PATH()).into(img);

                        if (FLAG_SUCCESS.equals("1")){
                            radioGroup.check(R.id.radio1);
                        } else {
                            radioGroup.check(R.id.radio2);
                        }
                        String longitude = repairOrderInfoBean.getLONGITUDE();//经度
                        String latitude = repairOrderInfoBean.getLATITUDE();//纬度
//                        location_addr.setText(repairOrderInfoBean.get);
                        imgPath=repairOrderInfoBean.getPIC_PATH();
                        ImageLoader.getInstance().displayImage(IMG_URL+imgPath,img);
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

            }
        });
    }



}
