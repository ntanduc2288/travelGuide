package com.travel.travelguide.services;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.travel.travelguide.Ulti.GCMRegistrationUtils;

/**
 * Created by user on 6/7/16.
 */
public class GcmInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        GCMRegistrationUtils gcmRegistrationUtils = new GCMRegistrationUtils(this);
        gcmRegistrationUtils.setUpGcmNotification();
    }
}