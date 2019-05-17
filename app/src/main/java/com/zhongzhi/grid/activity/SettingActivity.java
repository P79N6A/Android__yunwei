package com.zhongzhi.grid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongzhi.grid.R;

/**
 * Created by Administrator on 2018/7/9.
 */

public class SettingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.elect);
        title.setText("设置");
        TextView about = (TextView)findViewById(R.id.about);
        about.setOnClickListener(this);
        TextView modifyPassword = (TextView)findViewById(R.id.modify_password);
        modifyPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.about:
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.modify_password:
                Intent intent2 = new Intent();
                intent2.setClass(SettingActivity.this,ModifyPasswordActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
