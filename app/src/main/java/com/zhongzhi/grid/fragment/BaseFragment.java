package com.zhongzhi.grid.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongzhi.grid.R;
import com.zhongzhi.grid.activity.PermissionsActivity;
import com.zhongzhi.grid.util.PermissionsChecker;
import com.zhongzhi.grid.util.Tool;


/**
 * Created by ROG on 2016/7/2.
 */
public class BaseFragment extends Fragment implements View.OnClickListener{
    private AnimationDrawable anim;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    public String[] PERMISSIONS = new String[]{};
    public Dialog loadingDialog;
    public int dialogType =0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tool.translucentStatusBar(getActivity(),false);
        mPermissionsChecker = new PermissionsChecker(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void HttpErrorHint() {
        Toast.makeText(getActivity(), "网络信号不好，请稍后重试", Toast.LENGTH_SHORT).show();
    }

    /**
     * 得到根Fragment
     *
     * @return
     */
    private Fragment getRootFragment() {
        Fragment fragment = getParentFragment();
        while (fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        return fragment;

    }

    @Override
    public void onClick(View view) {

    }

    protected void enterActivity(Intent intent) {
        startActivity(intent);
//        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    /**
     * 权限开启
     */
    public boolean isPermissionCheckout(int requestCode){
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity(requestCode);
            return false;
        } else {
            return true;
        }

    }

    public void startPermissionsActivity(int requestCode) {
        PermissionsActivity.startActivityForResult(getActivity(),requestCode, PERMISSIONS);
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
}
