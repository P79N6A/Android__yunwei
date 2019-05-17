package com.zhongzhi.grid.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zhongzhi.grid.GDactivity.activity.service.LocationService;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.fragment.FragmentTab1;
import com.zhongzhi.grid.fragment.FragmentTab2;
import com.zhongzhi.grid.fragment.FragmentTab3;
import com.zhongzhi.grid.fragment.FragmentTab4;
import com.zhongzhi.grid.fragment.FragmentTab5;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.HttpUtil;
import com.zhongzhi.grid.util.SharedPrefsUtil;
import com.zhongzhi.grid.util.Tool;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import static com.zhongzhi.grid.activity.SplashActivity.LOCATION_CODE;
import static com.zhongzhi.grid.util.ToastUtils.showToast;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    private RadioGroup mRadioGroup;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragmentTab1 fragmentTab1;
    private FragmentTab2 fragmentTab2;
    private FragmentTab3 fragmentTab3;
    private FragmentTab4 fragmentTab4;
    private FragmentTab5 fragmentTab5;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        showFragment(0);

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setScanSpan(1000*60*10);
//可选，设置返回经纬度坐标类型，默认gcj02
//gcj02：国测局坐标；
//bd09ll：百度经纬度坐标；
//bd09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        option.setCoorType("wgs84");
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
        mLocationClient.start();

        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            if (ContextCompat.checkSelfPermission(MainActivity.this, mPermissionList[0]) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(MainActivity.this, mPermissionList[1])  != PackageManager.PERMISSION_GRANTED){
                Log.e("BRG","没有权限");
                // 没有权限，申请权限。
                // 申请授权。
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
//                        Toast.makeText(getActivity(), "没有权限", Toast.LENGTH_SHORT).show();
            }else{
                startService(new Intent(this, LocationService.class));
            }
        }else{
            //启动服务
            startService(new Intent(this, LocationService.class));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == LOCATION_CODE) {
            if (grantResults.length > 0) {

//                Log.i(TAG, "同意授权");
            //    GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
                for(int grantResult: grantResults){
                    if(grantResult != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //启动服务
                startService(new Intent(this, LocationService.class));
            } else {
//                Log.i(TAG, "拒绝授权");

            }
        }
    }

    @Override
    protected void initView() {
        super.initView();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        mRadioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.rb_1:
                showFragment(0);
                break;
            case R.id.rb_2:
                showFragment(1);
                break;
            case R.id.rb_3:
                showFragment(2);
                break;
            case R.id.rb_4:
                showFragment(3);
                break;
            case R.id.rb_5:
                showFragment(4);
                break;
        }
    }



    public void showFragment(int index){

        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch (index){

            case 0:

                /**
                 * 如果Fragment为空，就新建一个实例
                 * 如果不为空，就将它从栈中显示出来
                 */
                if (fragmentTab1==null){
                    fragmentTab1=new FragmentTab1();
                    ft.add(R.id.fragment_content,fragmentTab1,"fragmentTab1");
                }else {
                    ft.show(fragmentTab1);
                }

                break;
            case 1:
                if (fragmentTab2==null){
                    fragmentTab2=new FragmentTab2();
                    ft.add(R.id.fragment_content,fragmentTab2,"fragmentTab2");
                }else {
                    ft.show(fragmentTab2);
                }

                break;
            case 2:
                if (fragmentTab3==null){
                    fragmentTab3=new FragmentTab3();
                    ft.add(R.id.fragment_content,fragmentTab3,"fragmentTab3");
                }else {
//                    mShoppingCartFragment.clearCheckHashMap();
                    ft.show(fragmentTab3);
                }

                break;
            case 3:
                if (fragmentTab4==null){
                    fragmentTab4=new FragmentTab4();
                    ft.add(R.id.fragment_content,fragmentTab4,"fragmentTab4");
                }else {
                    ft.show(fragmentTab4);
                }

                break;
            case 4:
                if (fragmentTab5==null){
                    fragmentTab5=new FragmentTab5();
                    ft.add(R.id.fragment_content,fragmentTab5,"fragmentTab5");
                }else {
                    ft.show(fragmentTab5);
                }

                break;
        }

        ft.commitAllowingStateLoss();
    }

    public void hideFragment(FragmentTransaction ft){
        //如果不为空，就先隐藏起来
        if (fragmentTab1!=null){
            ft.hide(fragmentTab1);
        }
        if(fragmentTab2!=null) {
            ft.hide(fragmentTab2);
        }
        if(fragmentTab3!=null) {
            ft.hide(fragmentTab3);
        }
        if(fragmentTab4!=null) {
            ft.hide(fragmentTab4);
        }
        if(fragmentTab5!=null) {
            ft.hide(fragmentTab5);
        }
//        if(mMineFragment != null){
//            ft.hide(mMineFragment);
//        }
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            getData(location.getLongitude(),location.getLatitude());
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
        }
    }

    /**
     * 获取数据
     */
    private void getData(double longitude,double latitude) {
        if (!HttpUtil.isNetworkConnected(this)) {
            showToast(getString(R.string.net_hint));
            return;
        }

        JSONObject ob = new JSONObject();
        try {
            ob.put("org_num", SharedPrefsUtil.getValue(this,"org_num",null));
            ob.put("longitude", longitude+"");
            ob.put("latitude", latitude+"");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.SAVE_COORDINATE), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);

                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {

                    } else {

                    }

                } else {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
//                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
//                loadingDialog.dismiss();
            }

            @Override
            public void onFinished() {
//                loadingDialog.dismiss();
            }
        });
    }
}
