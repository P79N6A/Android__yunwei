package com.zhongzhi.grid.GDactivity.activity.dainjuhe;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;
import com.zhongzhi.grid.GDactivity.activity.RideRouteCalculateActivityNavi;
import com.zhongzhi.grid.GDactivity.activity.SingleRouteCalculateActivityNavi;
import com.zhongzhi.grid.GDactivity.activity.WalkRouteCalculateActivityNavi;
import com.zhongzhi.grid.GDactivity.activity.demo.RegionItem;
import com.zhongzhi.grid.GDactivity.activity.service.LocationService;
import com.zhongzhi.grid.GDactivity.activity.util.AnimationUtil;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.activity.BaseActivity;
import com.zhongzhi.grid.activity.RepairOrderInfoActivity;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.RepairOrderItem;
import com.zhongzhi.grid.bean.RepairOrderListBean;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.HttpUtil;
import com.zhongzhi.grid.util.SharedPrefsUtil;
import com.zhongzhi.grid.util.Tool;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhongzhi.grid.util.ToastUtils.showToast;

public class DianJuHeActivity extends BaseActivity implements ClusterRender,
        AMap.OnMapLoadedListener, ClusterClickListener  {
    private MapView mMapView;
    private AMap mAMap;
    private List<RepairOrderItem> list=new ArrayList<>();
    //定义点击新增一个mark
    private Marker curShowWindowMarker;
    //选择的mark真实坐标
    private LatLng latLng;
    private RelativeLayout go_layout;
    private Button rb_drive;
    private Button rb_bike;
    private Button rb_walk;
    private int clusterRadius = 100;
    private LocationManager lm;//【位置管理】
    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<Integer, Drawable>();
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private ClusterOverlay mClusterOverlay;
    private LatLng currentLocationLatLng;
    private boolean isFirst=true;
    //标题
    private TextView elect;
    private ImageView back;
    private String work_num;
    private  Button dealwork;
    private int status;

 //   private MyReceiver receiver=null;
    private AMapLocation aMapLocation;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        if(list.get(0).getLATITUDE()!=null&&list.get(0).getLONGITUDE()!=null) {
                            Double lat = Double.valueOf(list.get(0).getLATITUDE());
                            Double lng = Double.valueOf(list.get(0).getLONGITUDE());
                            LatLng latLng = new LatLng(lat, lng);
                            CoordinateConverter converter = new CoordinateConverter(DianJuHeActivity.this);
                            // CoordType.GPS 待转换坐标类型
                            converter.from(CoordinateConverter.CoordType.GPS);
                            // sourceLatLng待转换坐标点 LatLng类型
                            converter.coord(latLng);
                            // 执行转换操作
                            latLng = converter.convert();
                            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                        }
                            drawDot();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   // Toast.makeText(DianJuHeActivity.this,"success",Toast.LENGTH_SHORT).show();
                    break;
                case 1://获取数据null
                //  Toast.makeText(DianJuHeActivity.this,"aa",Toast.LENGTH_LONG).show();
                    mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(LocationService.amapLocation.getLatitude(),LocationService.amapLocation.getLongitude())));
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dian_ju_he);
        //得到系统的位置服务，判断GPS是否激活
        lm=(LocationManager) getSystemService(LOCATION_SERVICE);
        boolean ok=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(ok){
            //Toast.makeText(DianJuHeActivity.this,"GPS已经开启",Toast.LENGTH_LONG).show();
            // text1.setText("GPS已经开启");
        }else{
            showDialogGPSOpen(DianJuHeActivity.this);

        }
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        go_layout=findViewById(R.id.go_layout);
        rb_drive=findViewById(R.id.rb_drive);
        rb_bike=findViewById(R.id.rb_bike);
        rb_walk=findViewById(R.id.rb_walk);
       dealwork=findViewById(R.id.deal_work);
        //返回
        elect=findViewById(R.id.elect);
        elect.setText("低压运维巡检地图");
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        init();
        getData();
        goButton();

        /*//注册广播
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.ljq.activity.LocationService");
        registerReceiver(receiver, filter);*/
        LocationService.mLocationListener.onLocationChanged(LocationService.amapLocation);//显示蓝点，当前位置
    }


    //弹框判断GPS是否开启
    private void showDialogGPSOpen(final Context context) {
        View view = getLayoutInflater().inflate(R.layout.dialog_2, null);
        final TextView cancel = (TextView) view.findViewById(R.id.cancel);
        final TextView save = (TextView) view.findViewById(R.id.save);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "系统检测到未开启GPS定位服务", Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

    }
//选择导航模式
    private void goButton(){
        rb_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Poi end = new Poi("终点", latLng, "");
//                AmapNaviPage.getInstance().showRouteActivity(DianJuHeActivity.this, new AmapNaviParams(null, null, end, AmapNaviType.DRIVER), null);

               // Toast.makeText(DianJuHeActivity.this,""+latLng+"\n"+currentLocationLatLng,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(DianJuHeActivity.this, SingleRouteCalculateActivityNavi.class);
                Bundle bundle=new Bundle();
                bundle.putDouble("endLat",latLng.latitude);
                bundle.putDouble("endLng",latLng.longitude);
                bundle.putDouble("startLat",LocationService.amapLocation.getLatitude());
                bundle.putDouble("startLng",LocationService.amapLocation.getLongitude());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        rb_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DianJuHeActivity.this, RideRouteCalculateActivityNavi.class);
                Bundle bundle=new Bundle();
                bundle.putDouble("endLat",latLng.latitude);
                bundle.putDouble("endLng",latLng.longitude);
                bundle.putDouble("startLat",LocationService.amapLocation.getLatitude());
                bundle.putDouble("startLng",LocationService.amapLocation.getLongitude());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rb_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DianJuHeActivity.this, WalkRouteCalculateActivityNavi.class);
                Bundle bundle=new Bundle();
                bundle.putDouble("endLat",latLng.latitude);
                bundle.putDouble("endLng",latLng.longitude);
                bundle.putDouble("startLat",LocationService.amapLocation.getLatitude());
                bundle.putDouble("startLng",LocationService.amapLocation.getLongitude());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        dealwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             confirm(work_num);
            }
        });
    }

    /**
     * 本人处理
     */
    private void confirm(String worknum) {
        if (!HttpUtil.isNetworkConnected(DianJuHeActivity.this)) {
            showToast(getString(R.string.net_hint));
            return;
        }
        JSONObject ob = new JSONObject();
        try {
            ob.put("worknum",worknum);
            ob.put("organizCode", SharedPrefsUtil.getValue(DianJuHeActivity.this,"organizCode",null));
            ob.put("status", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        x.http().post(Tool.getParams(ob.toString(), DianJuHeActivity.this, Constants.CONFIRM), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        Intent intent=new Intent(DianJuHeActivity.this, RepairOrderInfoActivity.class);
                        intent.putExtra("worknum",work_num);
                     //   Toast.makeText(DianJuHeActivity.this,""+status,Toast.LENGTH_LONG).show();
                        intent.putExtra("status",3);
                        startActivity(intent);
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

    private void init() {
        if (mAMap == null) {
            // 初始化地图
            mAMap = mMapView.getMap();
        }
        if (mAMap != null) {
            setUpMap();
            UiSettings uiSettings = mAMap.getUiSettings();
            uiSettings.setLogoBottomMargin(-50);//隐藏logo
            //指南针
            mAMap.getUiSettings().setCompassEnabled(true);
            mAMap.setOnMapLoadedListener(this);
            //地图拖动
            mAMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
                @Override
                public void onTouch(MotionEvent motionEvent) {
                    if (curShowWindowMarker != null) {
                        curShowWindowMarker.remove();
                    }
                    if (isFirst = true) {
                        isFirst = false;
                    }
                }
            });

            //点击地图监听
            mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (curShowWindowMarker != null) {
                        curShowWindowMarker.remove();
                        go_layout.setVisibility(View.GONE);
                        go_layout.setAnimation(AnimationUtil.moveToViewBottom());
                    }
                    if (isFirst = true) {
                        isFirst = false;
                    }
                }
            });
        }
}

    /***********************开启定位小蓝点**************************/

    private void setUpMap() {
        initLocationStyle();
        //显示指南针
        mAMap.getUiSettings().setCompassEnabled(true);
        // 设置默认定位按钮是否显示,非必需设置。
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);

    }
    public void initLocationStyle(){
        MyLocationStyle myLocationStyle;
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked));
        //*myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色*//*
        myLocationStyle.strokeColor(STROKE_COLOR);
        myLocationStyle.radiusFillColor(FILL_COLOR);// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(1f);
        myLocationStyle.anchor(0.5f, 0.5f);
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setMyLocationEnabled(true);
    }

    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        //销毁资源
        if(mClusterOverlay!=null) {
            mClusterOverlay.onDestroy();
        }
        mMapView.onDestroy();
    }


    public void drawDot() {

                List<ClusterItem> items = new ArrayList<ClusterItem>();
                //故障点
        if(list.isEmpty()==false) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getLATITUDE() != null&&list.get(i).getLONGITUDE()!=null) {
                    double lat = Double.parseDouble(list.get(i).getLATITUDE());
                    double lon = Double.parseDouble(list.get(i).getLONGITUDE());
                    LatLng slatLng = new LatLng(lat, lon);
                    CoordinateConverter converter = new CoordinateConverter(DianJuHeActivity.this);
                    // CoordType.GPS 待转换坐标类型
                    converter.from(CoordinateConverter.CoordType.GPS);
                    // sourceLatLng待转换坐标点 LatLng类型
                    converter.coord(slatLng);
                    // 执行转换操作
                    LatLng sbLatLng = converter.convert();
                    LatLng latLng = new LatLng(sbLatLng.latitude, sbLatLng.longitude, false);
                    RegionItem regionItem = new RegionItem(latLng,
                            "test" + i);
                    items.add(regionItem);
                }
            }
            mClusterOverlay = new ClusterOverlay(mAMap, items,
                    dp2px(getApplicationContext(), clusterRadius),
                    getApplicationContext());
            mClusterOverlay.setClusterRenderer(DianJuHeActivity.this);
            mClusterOverlay.setOnClusterClickListener(DianJuHeActivity.this);
        }


    }
    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (ClusterItem clusterItem : clusterItems) {
            builder.include(clusterItem.getPosition());
        }
        LatLngBounds latLngBounds = builder.build();
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
        if (clusterItems.size() < 2) {
            int n=1;
            for (int i = 0; i < list.size(); i++) {
                double lat = Double.parseDouble(list.get(i).getLATITUDE());
                double lon = Double.parseDouble(list.get(i).getLONGITUDE());
                LatLng slatLng = new LatLng(lat, lon);
                CoordinateConverter converter = new CoordinateConverter(DianJuHeActivity.this);
                // CoordType.GPS 待转换坐标类型
                converter.from(CoordinateConverter.CoordType.GPS);
                // sourceLatLng待转换坐标点 LatLng类型
                converter.coord(slatLng);
                // 执行转换操作
               LatLng  sslatLng = converter.convert();
                if (marker.getOptions().getPosition().latitude == sslatLng.latitude && marker.getOptions().getPosition().longitude == sslatLng.longitude) {
                    if (curShowWindowMarker != null) {
                        curShowWindowMarker.remove();
                    }
                    latLng=sslatLng;
                    curShowWindowMarker = mAMap.addMarker(new MarkerOptions().position(latLng).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.mipmap.point))).title("台区名称:"+list.get(i).getAREA_NAME()).snippet( "资产编号："+list.get(i).getASSET_NUM()+"\n用户名称:"+list.get(i).getELEC_USER_NAME()+"\n故障类型:"+list.get(i).getDISPACH_EXPLAIN()+"\n当前位置故障个数："+n));
                    work_num=list.get(i).getWORK_LIST_NUM();
                    status=list.get(i).getWORK_LIST_PROCE();
                    curShowWindowMarker.setToTop();
                    curShowWindowMarker.showInfoWindow();
                    go_layout.setVisibility(View.VISIBLE);
                    //移动到原本位置
                    go_layout.setAnimation(AnimationUtil.moveToViewLocation());
                    n++;

                }
            }
        }
    }

//设置聚合点样式
    @Override
    public Drawable getDrawAble(int clusterNum) {
        int radius = dp2px(getApplicationContext(), 80);
        if (clusterNum == 1) {
            Drawable bitmapDrawable = mBackDrawAbles.get(1);
            if (bitmapDrawable == null) {
                bitmapDrawable = getApplication().getResources().getDrawable(R.drawable.mpoint);
                mBackDrawAbles.put(1, bitmapDrawable);
            }
            return bitmapDrawable;
        } else if (clusterNum < 5) {
            Drawable bitmapDrawable = mBackDrawAbles.get(2);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(159, 210, 154, 6)));
                mBackDrawAbles.put(2, bitmapDrawable);
            }

            return bitmapDrawable;
        } else if (clusterNum < 10) {
            Drawable bitmapDrawable = mBackDrawAbles.get(3);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(199, 217, 114, 0)));
                mBackDrawAbles.put(3, bitmapDrawable);
            }
            return bitmapDrawable;
        } else {
            Drawable bitmapDrawable = mBackDrawAbles.get(4);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(235, 215, 66, 2)));
                mBackDrawAbles.put(4, bitmapDrawable);
            }

            return bitmapDrawable;
        }
    }
   //设置圈的样式
    private Bitmap drawCircle(int radius, int color) {

        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
        paint.setColor(color);
        canvas.drawArc(rectF, 0, 360, true, paint);
        return bitmap;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 获取数据
     */
    private void getData() {
        if (!HttpUtil.isNetworkConnected(DianJuHeActivity.this)) {
            showToast(getString(R.string.net_hint));
            return;
        }

        JSONObject ob = new JSONObject();

        try {
            ob.put("organizcode", SharedPrefsUtil.getValue(DianJuHeActivity.this, "organizCode", null));
            ob.put("page", 1);
            ob.put("MANAGE_UNIT","供电单位");
            ob.put("AREA_NAME","台区名称");
            ob.put("DISPACH_EXPLAIN","故障类型");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), DianJuHeActivity.this, Constants.SELECT_ALL), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        RepairOrderListBean repairOrderListBean = new Gson().fromJson(bean.getData(), RepairOrderListBean.class);
                        list.addAll(repairOrderListBean.getResults());
                        if(list.size()>0){
                                Message message = new Message();
                                message.what = 0;
                                handler.sendMessage(message);

                        }else {
                            Message message = new Message();
                            message.what =1;
                            handler.sendMessage(message);
                        }
                    }
                } else {
                    showToast(bean.getMsg());

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    @Override
    public void onMapLoaded() {

    }
}