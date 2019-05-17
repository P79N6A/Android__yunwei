package com.zhongzhi.grid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongzhi.grid.R;

public class CorrectActivity extends Activity implements View.OnClickListener {

    private String AreaNum;
    private String AreaName;
    private TextView tai_qu_num;
    private TextView tai_qu_name;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct);
        AreaNum = getIntent().getExtras().getString("AreaNum");
        AreaName = getIntent().getExtras().getString("AreaName");
        tai_qu_num = findViewById(R.id.tai_qu_num);
        tai_qu_name = findViewById(R.id.tai_qu_name);
        back = findViewById(R.id.back);
        tai_qu_num.setText(AreaNum);
        tai_qu_name.setText(AreaName);
        back.setOnClickListener(CorrectActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
