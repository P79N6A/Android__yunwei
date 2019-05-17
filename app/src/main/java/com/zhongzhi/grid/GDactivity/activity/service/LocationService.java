package com.zhongzhi.grid.GDactivity.activity.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service {

    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public static AMapLocation amapLocation;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private boolean isStop = false;
    public   int intTimer=300;
    public  String strIsLogin="1";

    public static double lat;
    public static double lng;


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //设置精度模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //关闭缓存模式
        mLocationOption.setLocationCacheEnable(false);
//        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 触发定时器
        if (!isStop) {
            startTimer();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient!=null) {
            mLocationClient.stopLocation();
        }
        // 停止定时器
        if (isStop) {
            stopTimer();
        }
    }

    private void startTimer() {
        isStop = true;//定时器启动后，修改标识，关闭定时器的开关
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.d("经度:", "================================================");
//                    do {
//                        try {
//                            if (strIsLogin=="1"){
//                                mLocationClient.startLocation();
//                            }
//                            Thread.sleep(1000*intTimer);//3秒后再次执行
//                        } catch (InterruptedException e) {
//                            // TODO Auto-generated catch block
//                            return;
//                        }
//                    } while (isStop);
                }
            };
        }
        if (mTimer != null && mTimerTask != null) {
            mTimer.schedule(mTimerTask, 0, 10000);//执行定时器中的任务
        }
    }
    /**
     * 停止定时器，初始化定时器开关
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        isStop = false;//重新打开定时器开关
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //声明定位回调监听器
    public static AMapLocationListener mLocationListener = new AMapLocationListener() {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            amapLocation = aMapLocation;
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    aMapLocation.getLatitude();//获取纬度
                    aMapLocation.getLongitude();//获取经度
                    /*aMapLocation.getAccuracy();//获取精度信息
                    aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    aMapLocation.getCountry();//国家信息
                    aMapLocation.getProvince();//省信息
                    aMapLocation.getCity();//城市信息
                    aMapLocation.getDistrict();//城区信息
                    aMapLocation.getStreet();//街道信息
                    aMapLocation.getStreetNum();//街道门牌号信息
                    aMapLocation.getCityCode();//城市编码
                    aMapLocation.getAdCode();//地区编码
                    aMapLocation.getAoiName();//获取当前定位点的AOI信息*/

                    //这个是请求数据的接口：
                    lat = aMapLocation.getLatitude();
                    lng = aMapLocation.getLongitude();
                  /*  String gps_url = MyURL.loginUrl + String.format("gps.ashx?userid=%s&x=%s&y=%s&position=%s&company=%s&address=%s&state=%s&remark=%s",
                            MyURL.no, lng, lat, "5", MyURL.company, aMapLocation.getStreet(), "0", "0");
                    String m = MyHttpUtils.getTextFromUrl(gps_url);
*/

                    Log.d("经度===:", ""+lng);
                    Log.d("纬度===:", ""+lat);

                    /*//发送广播
                    Intent intent=new Intent();
                    intent.putExtra("location", aMapLocation);
                //    intent.putExtra("listener", (Serializable) mLocationListener);
                    intent.putExtra("lat", aMapLocation==null?"":aMapLocation.getLatitude()+"");
                    intent.putExtra("lon", aMapLocation==null?"":aMapLocation.getLongitude()+"");
                    intent.setAction("com.ljq.activity.LocationService");
                    sendBroadcast(intent);*/

                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
                // 应该停止客户端再发送定位请求
//                if (mLocationClient.isStarted()) {
//                    mLocationClient.onDestroy();
//                }
            }
        }
    };
}
