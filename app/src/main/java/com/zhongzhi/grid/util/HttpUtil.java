package com.zhongzhi.grid.util;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;


import com.zhongzhi.grid.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by ROG on 2015/8/29.
 */
public class HttpUtil {

    /**
     * 判断网络是不是连接状态
     * @param context
     * @return
     */

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * 判断网络是不是wifi
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
               if(mNetworkInfo.getType()== ConnectivityManager.TYPE_WIFI){
                   return true;
               } else {
                   return false;
               }
            } else {
                return false;
            }
        }
        return false;
    }

    public static Callback.Cancelable goRequest(final Context context, final Handler handler, RequestParams params, final Dialog loadingDialog){
        if (loadingDialog!=null )
            loadingDialog.show();
        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                    Message msg = new Message();
                    msg.obj = result;
                    msg.what = Constants.REQUEST_SUCCESS;
                    handler.sendMessage(msg);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                MyLog.e("HTTP",ex.toString());
                ToastUtils.showToast(context.getResources().getString(R.string.http_error));
                Message msg = new Message();
                msg.what = Constants.REQUEST_FAIL;
                handler.sendMessage(msg);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Message msg = new Message();
                msg.what = Constants.CANCEL;
                handler.sendMessage(msg);
            }

            @Override
            public void onFinished() {
                if (loadingDialog!=null && loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }
        });

        return cancelable;
    }
}
