package com.zhongzhi.grid.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.zhongzhi.grid.GridAplication;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.util.PermissionsChecker;
import com.zhongzhi.grid.util.Tool;

/**
 * Created by Administrator on 2017/7/15.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private AnimationDrawable anim;
    public int dialogType =0;
    GridAplication app = GridAplication.getInstance();
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    public Dialog loadingDialog;
    public Context context = this;
    // 所需的全部权限
    public String[] PERMISSIONS = new String[]{};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tool.translucentStatusBar(this,false);
        mPermissionsChecker = new PermissionsChecker(this);
//        initView();

    }

    protected void initView(){

    }

    @Override
    public void onClick(View v) {

    }



    /**
     * 权限开启
     */
    public boolean isPermissionCheckout(){
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
            return false;
        } else {
            return true;
        }

    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, 0, PERMISSIONS);
    }

    /**
     * 得到自定义的progressDialog
     * @param context
     * @param msg
     * @return
     */
    public Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.get_data_progressbar, null);// 得到加载view
        TextView tipTextView = (TextView) v.findViewById(R.id.tip_tv);// 提示文字
//        ImageView progressbar = (ImageView) v.findViewById(R.id.progressbar);
//        try {
//            progressbar.setBackgroundResource(R.drawable.drop_down_move);
//            anim = (AnimationDrawable) progressbar.getBackground();
//            anim.setOneShot(false);
//            anim.start();
//        }catch (OutOfMemoryError e){
//            e.printStackTrace();
//        }
        if (dialogType==0) {
            tipTextView.setText(getString(R.string.loading_hint));// 设置加载信息
        } else {
            tipTextView.setText(msg);// 设置加载信息
        }
        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(v);// 设置布局
        return loadingDialog;

    }

    public void dialogDismiss(){
        if(loadingDialog!=null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }
    public void dialogShow(){
        if(loadingDialog!=null && !loadingDialog.isShowing()){
            loadingDialog.show();
        }
    }

    protected void enterActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    protected void enterActivity(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    protected void exitActivity() {
        this.finish();
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
    }


    public void hinKkeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    @Override
    public void onBackPressed() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        exitActivity();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
