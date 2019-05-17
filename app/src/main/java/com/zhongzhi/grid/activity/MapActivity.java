package com.zhongzhi.grid.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.NormalUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/10.
 */

public class MapActivity extends BaseActivity {
    private static final String APP_FOLDER_NAME = "Grid";
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private String lat;
    private String lng;
    private String authinfo;
    private boolean hasInitSuccess;
    private BNRoutePlanNode sNode;
    private BNRoutePlanNode eNode;
    private TextView address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SDKInitializer.initialize(getApplicationContext());
        if (!"generic".equalsIgnoreCase(Build.BRAND)) {
            SDKInitializer.initialize(getApplicationContext());
        }
        setContentView(R.layout.activity_map);
        address=findViewById(R.id.address);
        lat = getIntent().getExtras().getString("lat");
        lng = getIntent().getExtras().getString("lng");

        if (Build.VERSION.SDK_INT >= 25) {
            String[] mPermissionList = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
        eNode = new BNRoutePlanNode(Double.parseDouble(lng), Double.parseDouble(lat), "", "", BNRoutePlanNode.CoordinateType.BD09LL);
        initView();
        if(initDirs()){
            initNavi();
        }


        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        builder.target(latLng).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                .newMapStatus(builder.build()));



    }



    @Override
    protected void initView() {
        super.initView();
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.elect);
        title.setText("地图");
        TextView daohang = (TextView) findViewById(R.id.save);
        daohang.setText("导航");
        daohang.setOnClickListener(this);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图 ,mBaiduMap是地图控制器对象
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                lat = String.valueOf(point.latitude);
                lng = String.valueOf(point.longitude);
                mBaiduMap.clear();
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                mBaiduMap.addOverlay(new MarkerOptions().position(
                        latLng)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.location_icon)));
                //根据坐标转换为地址
                GeoCoder mSearch = GeoCoder.newInstance();
                mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        String location = reverseGeoCodeResult.getAddress();
                        address.setText(location);
                    }
                });
                //下面是传入对应的经纬度
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        //可选，设置返回经纬度坐标类型，默认gcj02
//gcj02：国测局坐标；
//bd09ll：百度经纬度坐标；
//bd09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        option.setCoorType("bd09ll");
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
        mLocationClient.start();
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明


        LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        mBaiduMap.addOverlay(new MarkerOptions().position(
                latLng)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.location_icon)));

    }


    private void initNavi(){
        BaiduNaviManagerFactory.getBaiduNaviManager().init(this, Constants.BASE_PATH, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                MapActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MapActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void initStart() {
                Toast.makeText(MapActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void initSuccess() {
                Toast.makeText(MapActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;

                // 初始化tts
                initTTS();
            }

            @Override
            public void initFailed() {
                Toast.makeText(MapActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean initDirs() {
        File f = new File(Constants.BASE_PATH, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(getApplicationContext(),
                Constants.BASE_PATH, APP_FOLDER_NAME, NormalUtils.getTTSAppID());

        // 不使用内置TTS
//         BaiduNaviManagerFactory.getTTSManager().initTTS(mTTSCallback);

        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayError");
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.e("BNSDKDemo", "ttsHandler.msg.what=" + msg.what);
                    }
                }
        );
    }



    private void routeplanToNavi( ) {
        if (!hasInitSuccess) {
            Toast.makeText(MapActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }


//        mStartNode = sNode;

        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
        list.add(sNode);
        list.add(eNode);

        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(MapActivity.this, "算路开始", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(MapActivity.this, "算路成功", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(MapActivity.this, "算路失败", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(MapActivity.this, "算路成功准备进入导航", Toast.LENGTH_SHORT)
                                        .show();
                                Intent intent = new Intent(MapActivity.this,
                                        DemoGuideActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("routePlanNode", sNode);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

// 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

// 设置定位数据
            mBaiduMap.setMyLocationData(locData);

// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.mipmap.location_icon);
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true,null);
            mBaiduMap.setMyLocationConfiguration(config);
//            MapStatus.Builder builder = new MapStatus.Builder();
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            builder.target(latLng).zoom(18.0f);
//            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
//                    .newMapStatus(builder.build()));
            sNode = new BNRoutePlanNode(location.getLongitude(), location.getLatitude(), "", "", BNRoutePlanNode.CoordinateType.BD09LL);
//
//// 当不需要定位图层时关闭定位图层
//            mBaiduMap.setMyLocationEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                routeplanToNavi();
                break;

        }
    }
}