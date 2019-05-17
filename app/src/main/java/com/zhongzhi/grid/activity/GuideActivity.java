package com.zhongzhi.grid.activity;

/**
 * Created by Administrator on 2017/3/21.
 */


import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.zhongzhi.grid.R;
import com.zhongzhi.grid.adapter.ViewPagerAdapter;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @{# GuideActivity.java Create on 2013-5-2 下午10:59:08
 *
 *     class desc: 引导界面
 *
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @Author <a href="mailto:gaolei_xj@163.com">Leo</a>
 *
 *
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;

//    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;
    private LinearLayout pointLineLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        // 初始化页面
        initViews();

        // 初始化底部小点
        initDots();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.guide_1, null));
        views.add(inflater.inflate(R.layout.guide_2, null));
        views.add(inflater.inflate(R.layout.guide_3, null));

        pointLineLayout= (LinearLayout)findViewById(R.id.pointline);
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
        Button tiaoguo = (Button) findViewById(R.id.tiaoguo);
        tiaoguo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tiaoguo:
                SharedPrefsUtil.putValue(this, Constants.IS_FIRST_IN,false);
                // 跳转
                Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

        private void initDots() {


        dots = new ImageView[views.size()];

//        // 循环取得小点图片
//        for (int i = 0; i < views.size(); i++) {
//            ImageView pointImage = new ImageView(this);
//            pointImage.setPadding(Tool.dip2px(this,4),0,0,0);
//            if (i==0) {
//                pointImage.setImageResource(R.mipmap.point0);
//            } else {
//                pointImage.setImageResource(R.mipmap.point1);
//            }
//            pointImage.setTag(i);
//            pointLineLayout.addView(pointImage);
//        }

//        currentIndex = 0;
//        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }



    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
//        for (int i = 0; i < pointLineLayout.getChildCount(); i++) {
//            ((ImageView) pointLineLayout.getChildAt(i)).setImageResource(R.mipmap.point1);
//        }
//        ((ImageView) pointLineLayout.getChildAt(arg0)).setImageResource(R.mipmap.point0);
        currentIndex = arg0;
    }

}
