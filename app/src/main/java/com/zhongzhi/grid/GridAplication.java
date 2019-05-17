package com.zhongzhi.grid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;


import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zhongzhi.grid.util.Constants;

import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;


/**
 * Created by Administrator on 2017/7/15.
 */

public class GridAplication extends MultiDexApplication {
    private static GridAplication app;
    private static Context currentContext;
    private Handler handler = new Handler();

    public String clientId = "";
    @Override
    public void onCreate() {
        app = this;
        super.onCreate();
        x.Ext.init(app);
        x.Ext.setDebug(true);
//        SDKInitializer.initialize(getApplicationContext());
        File cacheDir = new File(Constants.BASE_PATH);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        initImageLoader(getApplicationContext());
//        final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

    }


    public static void initImageLoader(Context context) {
        //缓存文件的目录
//        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imperius/picture");
        File cacheDir = new File(Constants.BASE_PATH);
        if (!cacheDir.exists()) {
            boolean bl = cacheDir.mkdir();
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.mipmap.default_img_1_1)
                .showImageOnFail(R.mipmap.default_img_1_1)
                .showImageForEmptyUri(R.mipmap.default_img_1_1)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3) //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(50 * 1024 * 1024)  // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options)
                // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        //全局初始化此配置
        ImageLoader.getInstance().init(config);
    }

    public synchronized static GridAplication getInstance() {
        if (null == app) {
            app = new GridAplication();
        }
        return app;
    }

    public static Context getContext() {
        return app;
    }

    public static Context getCurrentContext() {
        return currentContext;
    }

    public void setCurrentContext(Context context) {
        this.currentContext = context;
    }

    /**
     * 分割 Dex 支持
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}