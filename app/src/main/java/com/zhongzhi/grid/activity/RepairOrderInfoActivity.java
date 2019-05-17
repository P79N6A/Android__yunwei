package com.zhongzhi.grid.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.zhongzhi.grid.GDactivity.activity.service.LocationService;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.RepairOrderInfoBean;
import com.zhongzhi.grid.loader.GlideImageLoader;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.HttpUtil;
import com.zhongzhi.grid.util.SharedPrefsUtil;
import com.zhongzhi.grid.util.Tool;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.zhongzhi.grid.util.ToastUtils.showToast;

/**
 * Created by Administrator on 2018/7/9.
 */

public class RepairOrderInfoActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener {

    public LocationClient mLocationClient = null;
    private TextView location_addr;
    private String worknum;
    private TextView orderNum;
    private TextView user_num;
    private TextView ziChanNum;
    private TextView userName;
    private TextView biaoxiangNum;
    private TextView taiquNum;
    private TextView taiquName;
    private TextView startTime;
    private TextView endTime;
    private TextView paidanren;
    private LinearLayout edit_layout;
    private Button conmmit;
    private RadioGroup radioGroup;
    private EditText pai_dan_shuoming;
    private EditText chuli_banfa;
    private ImageView img;
    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path = new ArrayList<>();
    //    private PhotoAdapter photoAdapter;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private BDLocation mBDLocation;
    private String succ = "1";
    private int status;
    private TextView save;
    private double longitude;
    private double latitude;
    private String AreaNum;
    private String AreaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_order_info);
        worknum = getIntent().getExtras().getString("worknum");
        status = getIntent().getExtras().getInt("status");
//        isEdit = getIntent().getExtras().getBoolean("isEdit");
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
        initGallery();
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
        user_num = findViewById(R.id.user_num);
        userName = (TextView)findViewById(R.id.user_name);
        biaoxiangNum = (TextView)findViewById(R.id.biao_xiang_num);
        taiquNum = (TextView)findViewById(R.id.tai_qu_num);
        taiquName = (TextView)findViewById(R.id.tai_qu_name);
        startTime = (TextView)findViewById(R.id.start_time);
        endTime = (TextView)findViewById(R.id.end_time);
        paidanren = (TextView)findViewById(R.id.pai_dan_ren);
        pai_dan_shuoming = (EditText)findViewById(R.id.pai_dan_shuoming);
        chuli_banfa = (EditText)findViewById(R.id.chuli_banfa);
        save = (TextView)findViewById(R.id.save);
        //save.setText("更正");
        save.setOnClickListener(this);
        conmmit = (Button) findViewById(R.id.conmmit);
        conmmit.setOnClickListener(this);
        if(status == 3){
            conmmit.setVisibility(View.VISIBLE);
        }else {
            conmmit.setVisibility(View.GONE);
        }
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        edit_layout = (LinearLayout)findViewById(R.id.edit_layout);
        img = (ImageView)findViewById(R.id.img);
        //处理办法编辑框显示光标
        chuli_banfa.requestFocus();
        img.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radio1:
                        succ = "1";
                        break;
                    case R.id.radio2:
                        succ = "0";
                        break;
                }
            }
        });
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
        //根据坐标逆转，显示地理信息
        setCurrentLocationDetails(new LatLng(LocationService.amapLocation.getLatitude(),LocationService.amapLocation.getLongitude()));
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.zhongzhi.grid.fileprovider")   // provider(必填)
                .pathList(path)                         // 记录已选的图片
                .multiSelect(false)                      // 是否多选   默认：false
                .multiSelect(false, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .crop(true)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(true, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")           // 图片存放路径
                .build();
    }


    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
//                Log.i(TAG, "onStart: 开启");
            }

            @Override
            public void onSuccess(List<String> photoList) {
//                Log.i(TAG, "onSuccess: 返回数据");
                path.clear();
                for (String s : photoList) {
//                    Log.i(TAG, s);
                    path.add(s);
                    Glide.with(context)
                            .load(s)
                            .centerCrop()
                            .into(img);
                }

//                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
//                Log.i(TAG, "onCancel: 取消");
            }

            @Override
            public void onFinish() {
//                Log.i(TAG, "onFinish: 结束");
            }

            @Override
            public void onError() {
//                Log.i(TAG, "onError: 出错");
            }
        };

    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img:
                galleryConfig.getBuilder().isOpenCamera(false).build();
                initPermissions();
                break;
            case R.id.save:
                correct();
                break;
            case R.id.conmmit:
                String way = chuli_banfa.getText().toString().trim();
                if(TextUtils.isEmpty(way)){
                    showToast("请填写处理办法");
                }else if(path.size() == 0){
                    showToast("请上传照片");
                }else {
                    chuli(way,path.get(0));
                }

                break;
        }
    }

    private void correct() {
        Intent intent = new Intent();
        intent.putExtra("AreaNum", AreaNum);
        intent.putExtra("AreaName", AreaName);
        intent.setClass(RepairOrderInfoActivity.this, CorrectActivity.class);
        startActivity(intent);
    }


    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.i(TAG, "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                Log.i(TAG, "拒绝过了");
                Toast.makeText(this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
//                Log.i(TAG, "进行授权");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
//            Log.i(TAG, "不需要授权 ");
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.i(TAG, "同意授权");
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
            } else {
//                Log.i(TAG, "拒绝授权");

            }
        }
    }


    public void enableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(false);
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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.SELECTDETAIL), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                loadingDialog.dismiss();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        RepairOrderInfoBean repairOrderInfoBean = new Gson().fromJson(bean.getData(),RepairOrderInfoBean.class);
//                        if(TextUtils.isEmpty(repairOrderInfoBean.getMID_OPERAT_PERSON())){
//                            save.setVisibility(View.GONE);
//                        }
                        orderNum.setText(repairOrderInfoBean.getWORK_LIST_NUM());
                        ziChanNum.setText(repairOrderInfoBean.getASSET_NUM());
                        user_num.setText(repairOrderInfoBean.getUSER_NUM());
                        userName.setText(repairOrderInfoBean.getELEC_USER_NAME());
                        biaoxiangNum.setText(repairOrderInfoBean.getMEASURE_BOX_ASSET_NUM());
                        taiquNum.setText(repairOrderInfoBean.getAREA_NUM());
                        taiquName.setText(repairOrderInfoBean.getAREA_NAME());
                        startTime.setText(repairOrderInfoBean.getBEGIN_DATE_TIME());
//                        endTime.setText();
                        paidanren.setText(repairOrderInfoBean.getDISPATCHER());
                        pai_dan_shuoming.setText(repairOrderInfoBean.getDISPACH_EXPLAIN());
                        AreaNum = taiquNum.getText().toString().trim();
                        AreaName = taiquName.getText().toString().trim();
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



    /**
     * 处理
     */
    private void chuli(String way,String filePath) {
        if (!HttpUtil.isNetworkConnected(this)) {
            showToast(getString(R.string.net_hint));
            return;
        }
        loadingDialog.show();
        JSONObject ob = new JSONObject();
        try {
            if(mBDLocation != null){

            }
            ob.put("longitude",LocationService.amapLocation.getLongitude());
            ob.put("latitude",LocationService.amapLocation.getLatitude());
            ob.put("organizcode", SharedPrefsUtil.getValue(this,"organizCode",null));
            ob.put("succ", succ);
            ob.put("way", way);
            ob.put("worknum", worknum);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        RequestParams params = Tool.getParams(ob.toString(), this, Constants.ADDTODAY);
        params.setMultipart(true);
        params.addBodyParameter("images",new File(filePath));
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                loadingDialog.dismiss();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        showToast(bean.getMsg());
                        setResult(1);
                        Intent intent=new Intent(RepairOrderInfoActivity.this,MainActivity.class);
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

    /************************坐标转地址逆编码****************************/
    private void setCurrentLocationDetails(LatLng latLng){
        LatLonPoint latLonPoint=new LatLonPoint(latLng.latitude,latLng.longitude);
// 地址逆解析
       GeocodeSearch geocoderSearch = new GeocodeSearch(RepairOrderInfoActivity.this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        // 第一个参数表示一个Latlng(经纬度)，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    //实现GeocodeSearch.OnGeocodeSearchListener重写其方法
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        String formatAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
        location_addr.setText(formatAddress);
        Log.e("formatAddress", "formatAddress:"+formatAddress);
        Log.e("formatAddress", "rCode:"+i);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    /****************************************************************/

}
