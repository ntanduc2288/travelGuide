package com.travel.travelguide.presenter.MapGuide;

import android.location.Location;

import com.google.android.gms.maps.model.Marker;
import com.travel.travelguide.Object.User;

import java.util.ArrayList;

/**
 * Created by user on 4/24/16.
 */
public interface IMapGuideView {
    void displayLocation(Location lastLocation);
    ArrayList<Marker> displayMarkers(ArrayList<User> users);
    void zoomBound(ArrayList<Marker> markers);
    void showError(String errorMessage);
}
