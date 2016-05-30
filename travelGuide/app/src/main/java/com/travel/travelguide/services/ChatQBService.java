package com.travel.travelguide.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by user on 5/30/16.
 */
public class ChatQBService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
