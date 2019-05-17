package com.zhongzhi.grid.GDactivity.activity;

import android.os.Bundle;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class SingleRouteCalculateActivityNavi extends NaviBaseActivity {

    private Double endLat;
    private Double endLng;
    private Double startLat;
    private Double startLng;
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();

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
        startList.add(new NaviLatLng(startLat, startLng));
        endList.add(new NaviLatLng(endLat, endLng));
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(startList,endList, mWayPointList, strategy);

    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }
}
