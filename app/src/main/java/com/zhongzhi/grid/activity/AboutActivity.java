package com.zhongzhi.grid.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongzhi.grid.R;

/**
 * Created by Administrator on 2018/7/9.
 */

public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.elect);
        title.setText("关于");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
