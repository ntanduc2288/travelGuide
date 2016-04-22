package com.travel.travelguide;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.backendless.Backendless;

/**
 * Created by user on 4/22/16.
 */
public class MyApp extends Application {

    final String backendlessAppId = "404AF5BC-38D5-B76F-FF51-58E77AEA4500";
    final String backendlessSecretKey = "9050B7F0-D660-1F08-FFE2-EE0F25F84400";
    final String backendlessVersion = "v1";
    @Override
    public void onCreate() {
        super.onCreate();
        initBackendless();
        test();
    }

    private void test() {

    }

    private void initBackendless() {
        Backendless.initApp(getApplicationContext(), backendlessAppId, backendlessSecretKey, backendlessVersion);
        //
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
