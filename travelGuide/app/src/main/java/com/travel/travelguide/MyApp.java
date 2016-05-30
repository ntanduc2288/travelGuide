package com.travel.travelguide;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.backendless.Backendless;
import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.quickblox.core.QBSettings;

import io.fabric.sdk.android.Fabric;

/**
 * Created by user on 4/22/16.
 */
public class MyApp extends Application {

    final String backendlessAppId = "404AF5BC-38D5-B76F-FF51-58E77AEA4500";
    final String backendlessSecretKey = "9050B7F0-D660-1F08-FFE2-EE0F25F84400";
    final String backendlessVersion = "v1";

    final String APP_ID = "41448";
    final String AUTH_KEY = "5rzFKJE7dW5yeej";
    final String AUTH_SECRET = "km-cmTV5K53zXp8";
    final String ACCOUNT_KEY = "bRa24gx5BFx5WuyyfGh7";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initBackendless();
        initQB();
        initImageloader();
    }

    private void initImageloader() {
        DisplayImageOptions opts = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading_icon)
                .showImageOnFail(R.drawable.anonymous_icon)
                .showImageForEmptyUri(R.drawable.anonymous_icon)
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true).build();


        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
//		config.memoryCache(new LruMemoryCache(2 * 1024 * 1024));
//        config.memoryCacheSize(2 * 1024 * 1024) ;
        config.threadPoolSize(3);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.diskCacheFileCount(100);
        // config.discCache(diskCache)(new UnlimitedDiskCache(cacheDir));
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        config.defaultDisplayImageOptions(opts);
        config.imageDownloader(new BaseImageDownloader(getApplicationContext()));


        // Initialize ImageLoader with configuration.
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(
                config.build());
    }

    private void initBackendless() {
        Backendless.initApp(getApplicationContext(), backendlessAppId, backendlessSecretKey, backendlessVersion);
        //
    }

    private void initQB(){
        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
