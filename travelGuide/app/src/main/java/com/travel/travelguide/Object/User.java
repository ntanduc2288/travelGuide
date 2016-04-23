package com.travel.travelguide.Object;

import com.backendless.BackendlessUser;
import com.travel.travelguide.Ulti.Constants;

/**
 * Created by user on 4/23/16.
 */
public class User extends BackendlessUser {
    String id;
    String name;
    String facebookLink;
    String email;
    String password;
    double latitude;
    double longtitude;
    String locationName;

    public User(){

    }

    public User(BackendlessUser backendlessUser){
        setId(backendlessUser.getUserId());
        setName((String) backendlessUser.getProperty(Constants.KEY_NAME));
        setEmail(backendlessUser.getEmail());
        setFacebookLink((String) backendlessUser.getProperty(Constants.KEY_FACEBOK_LINK));
        setLatitude((Double) backendlessUser.getProperty(Constants.KEY_LAT));
        setLongtitude((Double) backendlessUser.getProperty(Constants.KEY_LON));
        setLocationName((String) backendlessUser.getProperty(Constants.KEY_LOCATION_NAME));
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
        setProperty(Constants.KEY_LOCATION_NAME, locationName);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        setProperty(Constants.KEY_LAT, latitude);
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
        setProperty(Constants.KEY_LON, longtitude);
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
}
