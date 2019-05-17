package com.zhongzhi.grid.GDactivity.activity;

import android.os.Bundle;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.util.StatusBarUtil;


public class RideRouteCalculateActivityNavi extends NaviBaseActivity {
    private Double endLat;
    private Double endLng;
    private Double startLat;
    private Double startLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_navi);
        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.theme_color);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        Bundle bundle = getIntent().getExtras();
        endLat = bundle.getDouble("endLat");
        endLng=bundle.getDouble("endLng");
        startLat=bundle.getDouble("startLat");
        startLng=bundle.getDouble("startLng");
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        mAMapNavi.calculateRideRoute(new NaviLatLng(startLat, startLng), new NaviLatLng(endLat, endLng));
    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }
}
