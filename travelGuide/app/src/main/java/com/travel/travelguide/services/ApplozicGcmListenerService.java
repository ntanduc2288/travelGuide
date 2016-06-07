package com.travel.travelguide.services;

import android.os.Bundle;
import android.util.Log;

import com.applozic.mobicomkit.api.notification.MobiComPushReceiver;
import com.google.android.gms.gcm.GcmListenerService;
import com.travel.travelguide.Ulti.LogUtils;

/**
 * Created by user on 6/7/16.
 */
public class ApplozicGcmListenerService extends GcmListenerService {

    private static final String TAG = "ApplozicGcmListener";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        LogUtils.logD(ApplozicGcmListenerService.class.getSimpleName(), "onMessageReceived: " + data.toString());
        if (MobiComPushReceiver.isMobiComPushNotification(data)) {
            Log.i(TAG, "Applozic notification processing...");
            MobiComPushReceiver.processMessageAsync(this, data);
            return;
        }
    }

}
