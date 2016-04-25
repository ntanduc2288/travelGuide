package com.travel.travelguide.Object;

import com.backendless.BackendlessUser;
import com.backendless.geo.GeoPoint;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.Ulti.Ulti;

/**
 * Created by user on 4/23/16.
 */
public class User extends BackendlessUser {
    String id;
    String name;
    String facebookLink;
    String email;
    String password;
    String locationName;
    GeoPoint geoPoint;

    public User(){

    }

    public User(BackendlessUser backendlessUser){
        setId(backendlessUser.getUserId());
        setName((String) backendlessUser.getProperty(Constants.KEY_NAME));
        setEmail(backendlessUser.getEmail());
        setFacebookLink((String) backendlessUser.getProperty(Constants.KEY_FACEBOK_LINK));
        setlocation(Ulti.extractGeoPoint(backendlessUser));
        setLocationName((String) backendlessUser.getProperty(Constants.KEY_LOCATION_NAME));
    }



    public GeoPoint getLocation() {
        return geoPoint;
    }

    public void setlocation(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
        setProperty(Constants.KEY_LOCATION, geoPoint);
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
        setProperty(Constants.KEY_LOCATION_NAME, locationName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setProperty(Constants.KEY_OBJECT_ID, id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.setProperty(Constants.KEY_NAME, name);
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
        setProperty(Constants.KEY_FACEBOK_LINK, facebookLink);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
        setProperty(Constants.KEY_EMAIL, email);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
        setProperty(Constants.KEY_PASSWORD, password);
    }

    public BackendlessUser getBackendlessUser(){
        BackendlessUser backendlessUser = new BackendlessUser();
        backendlessUser.setProperties(getProperties());
        return backendlessUser;
    }
}
