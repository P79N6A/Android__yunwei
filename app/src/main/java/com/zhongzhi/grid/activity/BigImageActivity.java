package com.zhongzhi.grid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.util.StatusBarUtil;

import static com.zhongzhi.grid.util.Constants.IMG_URL;

public class BigImageActivity extends Activity {
    private TextView textView_exit;
    private ImageView imageView;

    private String imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.theme_color);
        imageView = findViewById(R.id.image);
        textView_exit = findViewById(R.id.exit);
        textView_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        imgURL=bundle.getString("imgURL");
        ImageLoader.getInstance().displayImage(IMG_URL+imgURL,imageView);
    }
}
