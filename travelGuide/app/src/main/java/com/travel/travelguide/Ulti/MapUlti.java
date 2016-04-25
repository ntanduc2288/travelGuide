package com.travel.travelguide.Ulti;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by user on 4/25/16.
 */
public class MapUlti {
    public static float getRadius(GoogleMap googleMap){
        Location locationCenter = getCenterLocation(googleMap);
        Location middleLeftCornerLocation = new Location("MiddleLeft");
        middleLeftCornerLocation.setLatitude(locationCenter.getLatitude());
        double bottom = googleMap.getProjection().getVisibleRegion().latLngBounds.southwest.longitude;
        middleLeftCornerLocation.setLongitude(bottom);

        float radius = locationCenter.distanceTo(middleLeftCornerLocation)/1000;
        radius += radius*0.5;
        return radius;

    }

    public static Location getCenterLocation(GoogleMap googleMap){
        LatLng latLngCenter = googleMap.getCameraPosition().target;
        Location centerLocation = new Location("center");
        centerLocation.setLatitude(latLngCenter.latitude);
        centerLocation.setLongitude(latLngCenter.longitude);
        return centerLocation;
    }
}
