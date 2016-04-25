package com.travel.travelguide.Ulti;

import android.text.TextUtils;

import com.backendless.BackendlessUser;
import com.backendless.geo.GeoPoint;

import java.util.Map;

/**
 * Created by user on 4/22/16.
 */
public class Ulti {
    public static boolean isEmailValid(String email) {
        if(TextUtils.isEmpty(email)){
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static GeoPoint extractGeoPoint(BackendlessUser backendlessUser){
        Object locationObject = backendlessUser.getProperty(Constants.KEY_LOCATION);
        if(locationObject instanceof GeoPoint){
            return (GeoPoint) locationObject;
        }else {
            Map<String, Object> map = (Map<String, Object>) backendlessUser.getProperty(Constants.KEY_LOCATION);
            GeoPoint geoPoint = new GeoPoint();
            geoPoint.setLatitude((Double) map.get("latitude"));
            geoPoint.setLongitude((Double) map.get("longitude"));
            geoPoint.setObjectId((String) map.get("objectId"));
            return geoPoint;
        }
    }


}
