package com.travel.travelguide.presenter.MapGuide;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.Marker;
import com.travel.travelguide.Object.User;

import java.util.ArrayList;

/**
 * Created by user on 4/24/16.
 */
public interface IMapGuideView {
    void showLoadingMarkerProcess();
    void hideLoadindMarkerProcess();
    void showLoading();
    void hideLoading();
    void displayLocation(Location lastLocation);
    ArrayList<Marker> displayMarkers(ArrayList<User> users);
    void showError(String errorMessage);
    void zoomToLevel(CameraUpdate cameraUpdate);
    void zoomToLevel(float level);
    void gotoProfileScreen(User user);
    void gotoLoginScreen();
    void showMessage(String message);
    void updateUserView(User user);
    Context getContext();
}
