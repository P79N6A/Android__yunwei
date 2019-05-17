package com.zhongzhi.grid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;


import com.zhongzhi.grid.R;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.SharedPrefsUtil;

import org.xutils.ex.DbException;




public class SplashActivity extends BaseActivity {


    private int LOGIN_RESULT_CODE = 100;
    private int GOOGLE_PLAY_RESULT_CODE = 200;
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private Handler mHandler = new Handler();

    public static final int LOCATION_CODE = 9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();


    }




    private void init(){

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(SharedPrefsUtil.getValue(SplashActivity.this, Constants.IS_FIRST_IN,true)){
//
//                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else {
                if(SharedPrefsUtil.getValue(SplashActivity.this, Constants.IS_FIRST_IN,true)){
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    if(SharedPrefsUtil.getValue(SplashActivity.this,"organizCode",null) == null){
//            LoginBusiness.loginIm(SqlUtil.getUser().getIdentify(), SqlUtil.getUser().getUsersig(), this);
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

// else {
//
//
//                    }


            }
        },3000);

    }


}
