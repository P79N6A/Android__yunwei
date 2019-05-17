package com.zhongzhi.grid.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zhongzhi.grid.R;

import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/4.
 */

public class Tool {

    /**
     * 获取屏幕宽度
     */

    public static int getwindowWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();

        return width;

    }

    /**
     * 获取屏幕宽度
     */

    public static int getwindowHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();

        return height;

    }

    public static String getVersion(Context context) {
        PackageInfo info = null;
        String currentVersion ="";
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersion = info.versionName;

//            MyLog.d("Tool", "the currentVersion is ==>" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return currentVersion;
    }

    public static RequestParams getParams(String data, Context context, String url, String tk){
        StringBuilder builder = new StringBuilder();
        Long currentTime = System.currentTimeMillis();
        builder.append("msh");
        builder.append("timestamp").append(currentTime);
        builder.append("plat").append("android");
        builder.append("v").append(Tool.getVersion(context));
        builder.append("data").append(data);
        builder.append("msh");
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("timestamp", currentTime + "");
        params.addBodyParameter("plat", "android");
        params.addBodyParameter("tk", tk);
        params.addBodyParameter("v", Tool.getVersion(context));
        params.addBodyParameter("data", data);
        params.addBodyParameter("sign", MD5.md5(builder.toString()));
        return params;
    }
    public static RequestParams getParams(String data, Context context, String url){
        StringBuilder builder = new StringBuilder();
        Long currentTime = System.currentTimeMillis();
        builder.append("msh");
        builder.append("timestamp").append(currentTime);
        builder.append("plat").append("android");
        builder.append("v").append(Tool.getVersion(context));
        builder.append("data").append(data);
        builder.append("msh");
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("timestamp", currentTime + "");
        params.addBodyParameter("plat", "android");
        params.addBodyParameter("v", Tool.getVersion(context));
        params.addBodyParameter("data", data);
        params.addBodyParameter("sign", MD5.md5(builder.toString()));
        return params;
    }
//    private void initComParams(String data,Context context, String tk,String url) {
//        RequestParams params = new RequestParams(url);
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        params.addBodyParameter("plat", "android");
//        params.addBodyParameter("timestamp", timestamp);
//        params.addBodyParameter("v",Tool.getVersion(context));
//        String sign = "";
//        StringBuilder builder = new StringBuilder();
//        builder.append(Default_AddStr);
//        builder.append("timestamp").append(timestamp);
//        builder.append("plat").append("android");
//        builder.append("v").append(BaseApplication.MyVersionName);
//        builder.append("data").append(data);
//        builder.append(Default_AddStr);
//        sign = MD5.md5(builder.toString());
//        if (!TextUtils.isEmpty(tk)) {
//            params.addBodyParameter("tk", tk);
//        }
//        params.addBodyParameter("sign", sign);
//        Log.d("param", "jsonString url = " + builder.toString() + "     tk= " + tk);
//    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    /** string字符串获取**/
    public static String getStringShared(Context context, String str) {
        String result = "";
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                context.MODE_PRIVATE);
        result = sp.getString(str, "");
        return result;
    }


    /** string字符串保存 **/
    public static void setStringShared(Context context, String str,
                                       String result) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(str, result);
        edit.commit();
    }
    /** string字符串保存 **/
    public static void setBooleanShared(Context context, String str,
                                        boolean result) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(str, result);
        edit.commit();
    }
    /** string字符串获取**/
    public static boolean getBooleanShared(Context context, String str) {
        boolean result = true;
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                context.MODE_PRIVATE);
        result = sp.getBoolean(str, false);
        return result;
    }

    /** string字符串获取**/
    public static boolean getBooleanShareddefaultTrue(Context context, String str) {
        boolean result = true;
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                context.MODE_PRIVATE);
        result = sp.getBoolean(str, true);
        return result;
    }


    /** string字符串获取**/
    public static Integer getIntShared(Context context, String str) {
        int result = 0;
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                context.MODE_PRIVATE);
        result = sp.getInt(str, 0);
        return result;
    }

    /** string字符串保存 **/
    public static void setIntShared(Context context, String str,
                                    int result) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(str, result);
        edit.commit();
    }


    /**
     * 根据手机的分辨率�?dp 的单�?转成�?px(像素)
     *
     * @param context
     *            dpValue
     * @return int
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    public static void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 4;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }




    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        if(hideStatusBarBackground){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {            //系统版本大于19
            setTranslucentStatus(activity);
        }
//        if (Build.VERSION.SDK_INT >= 21) {
//            ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
//            if (supportActionBar != null) {
//                supportActionBar.setElevation(0);
//            }
//        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.theme_color);
//        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        Class clazz = activity.getWindow().getClass();
//        try {
//            int darkModeFlag = 0;
//            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
//            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
//            darkModeFlag = field.getInt(layoutParams);
//            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
//            if(true){
//                extraFlagField.invoke(activity.getWindow(),darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
//            }else{
//                extraFlagField.invoke(activity.getWindow(), 0, darkModeFlag);//清除黑色字体
//            }
//        }catch (Exception e){
//          e.printStackTrace();
//        }
    }
    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
        winParams.flags |= bits;        // a|=b的意思就是把a和b按位或然后赋值给a   按位或的意思就是先把a和b都换成2进制，然后用或操作，相当于a=a|b
//        } else {
//            winParams.flags &= ~bits;        //&是位运算里面，与运算  a&=b相当于 a = a&b  ~非运算符
//        }
        win.setAttributes(winParams);
    }


    /**
     * 处理图片,按照宽度等比例拉伸
     *
     * @param bm
     *            所要转换的bitmap
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newHeight) {
        Bitmap newbm = null;
        try {
            if (bm == null) {
                return bm;
            }
            // 获得图片的宽高
            int width = bm.getWidth();
            int height = bm.getHeight();
            // 计算缩放比例
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleHeight, scaleHeight);
            try {
                newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                        true);
            } catch (Exception e) {
                // TODO: handle exception
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
        } catch (OutOfMemoryError e) {
            // TODO: handle exception
        }

        return newbm;
    }
    /**
     * 请求存储权限，在华为等6.0手机适配
     *
     * @param activity
     */
    public static void checkStoragePer(Activity activity) {
        try {
            final int REQUEST_EXTERNAL_STORAGE = 1;
            String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    /**
     * 判断手机号格式
     * @param mobiles
     * @return
     */
    public static boolean isPhoneNO(String mobiles) {
        Pattern p = Pattern
                //.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                .compile("^((13[0-9])|(15[^4,\\D])|(17[0,6-8])|(18[0-9])|(14[5,7]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    /**
     * 校验密码
     * @param password
     * @return
     */
    public static boolean isvalidPassword(String password) {
        Pattern p = Pattern
                //.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                .compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }


    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String formatDistance(int distance){
        String str = "";
        if(distance<1000){
            str = distance+"m";
        }else {
            DecimalFormat df   =new   DecimalFormat("0.00");
            str = df.format(distance/1000f)+"km";
        }
        return str;
    }

    /**
     * 是否是手机号
     *
     * @param mobiles
     * @return boolean(true为手机号)
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                // .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                .compile("^((13[0-9])|(15[^4,\\D])|(17[0,6-8])|(18[0-9])|(14[5,7]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 通过获取指定颜色的文本
     *
     * @param colorResource
     * @param text
     * @return
     */
    public static String getColorHtmlText(Context context, int colorResource, String text) {
        return "<font color='" + "#" + context.getResources().getString(colorResource).substring(3) + "'>" + text
                + "</font>";
    }

    /**
     * 保留两位小数
     * @param d
     * @return
     */
    public static String priceFormat(double d) {
        DecimalFormat df   = new DecimalFormat("######0.00");
        String s = df.format(d);
        return s;
    }

    /**
     * 保留2位小数 ，四舍五入
     * @return
     */
    public static String getRoundingPrice(double d) {
        String str  ="";
        BigDecimal mData = new BigDecimal(Double.toString(d)).setScale(2, BigDecimal.ROUND_HALF_UP);
        str = mData.toString();
        return str ;
    }
    public static String formatDate(long date, String format){
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 加密
     * @param map
     * @return
     */
    public static String sign(Map<String, String> map){
        Map<String, String> resultMap = sortMapByKey(map);    //按Key进行排序
        StringBuffer sb = new StringBuffer();
        int i=0;
        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            i=i+1;
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
            if(i==resultMap.entrySet().size()){
                sb.append("key=3c8f7f66b1c8f4cef872f6371c9020237329da2");
            }

        }
//        MyLog.e("ddd","qian====("+sb.toString());
        return encode(sb.toString());
    }
    /**
     * 加密
     * @return
     */
    public static String sign(){
        return encode("3c8f7f66b1c8f4cef872f6371c9020237329da2");
    }
    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

    static class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }

    /*
	 * 加密算法
	 */
    public static String encode(String text){

        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuilder sb =new StringBuilder();
            for(byte b:result){
                int number = b&0xff;
                String hex = Integer.toHexString(number);
                if(hex.length() == 1){
                    sb.append("0"+hex);
                }else{
                    sb.append(hex);
                }
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "" ;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }
    /**
     * convert px to its equivalent dp
     *
     * 将px转换为与之相等的dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale =  context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     * @param bitmap 原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file)  {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            }catch (FileNotFoundException e) {
                e.printStackTrace();
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            } finally {

                try {
                    fis.close();
                } catch (Exception ignored) {
                    // ignore exceptions generated by close()
                }

            }

        } else {
            return 0;
        }
        return size;
    }




    // 缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public static Bitmap getImageThumbnail(Bitmap bitmap, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        options.inJustDecodeBounds = false; // 设为 false
//        if(bitmap.getWidth()>=bitmap.getHeight()){
        float scale = width/(float)bitmap.getWidth();
//        }else{
//            scale = height/(float)bitmap.getHeight();
//        }

        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);

        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return bitmap;
    }

    /* 读取Assets文件夹中的图片资源
 * @param context
 * @param fileName 图片名称
 * @return
         */
    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


}
