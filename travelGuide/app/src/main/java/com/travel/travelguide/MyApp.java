package com.travel.travelguide;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.backendless.Backendless;
import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.travel.travelguide.Object.RatingEntityObject;

import io.fabric.sdk.android.Fabric;

import static com.travel.travelguide.Ulti.Constants.RATING_TABLE;

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
        initApplozic();
        initImageloader();
    }


    private void initApplozic() {
//        Show/Hide Green Dot for Online
        ApplozicSetting.getInstance(getApplicationContext()).hideOnlineStatusInMasterList();

//        Show/hide 'Start New Conversation' Plus (+) Button
        ApplozicSetting.getInstance(getApplicationContext()).hideStartNewButton();

//        Show/hide 'Start New' FloatingActionButton
        ApplozicSetting.getInstance(getApplicationContext()).hideStartNewFloatingActionButton();

//        For Group Add Member Button Hide
        ApplozicSetting.getInstance(getApplicationContext()).setHideGroupAddButton(true);

//        For Group Exit Button Hide
        ApplozicSetting.getInstance(getApplicationContext()).setHideGroupExitButton(true);

//        For Group Name Change Button Hide
        ApplozicSetting.getInstance(getApplicationContext()).setHideGroupNameEditButton(true);

//        For Group Memebr Remove Option Hide
        ApplozicSetting.getInstance(getApplicationContext()).setHideGroupRemoveMemberOption(true);

        ApplozicClient.getInstance(getApplicationContext()).hideChatListOnNotification();

        ApplozicSetting.getInstance(getApplicationContext()).hideStartNewButton();

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
        Backendless.Data.mapTableToClass(RATING_TABLE, RatingEntityObject.class);
        //
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
