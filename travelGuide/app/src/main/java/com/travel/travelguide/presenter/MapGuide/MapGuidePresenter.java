package com.travel.travelguide.presenter.MapGuide;

import android.location.Location;

import com.travel.travelguide.presenter.IBasePresenter;

/**
 * Created by user on 4/24/16.
 */
public interface MapGuidePresenter extends IBasePresenter {
    /**
     * Called in onMapReady()
     */
    void checkPlayServices();
    void buildGoogleApiClient();
    void createLocationRequest();
    /**
     * Called in onstart()
     */
    void connect();
    /**
     * Called in onStop()
     */
    void disConnect();
    /**
     * Called in onResume()
     */
    void startLocationUpdates();
    /**
     * Called in onPause()
     */
    void stopLocationUpdates();

    void getUserList(Location location);

    void getUserListWithRadius(float radius);

}
