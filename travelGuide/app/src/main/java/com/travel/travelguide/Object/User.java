package com.travel.travelguide.Object;

import com.backendless.BackendlessUser;
import com.backendless.geo.GeoPoint;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.Ulti.Ulti;

/**
 * Created by user on 4/23/16.
 */
@DatabaseTable(tableName = "User")
public class User extends BackendlessUser {
    @DatabaseField
    String id;
    @DatabaseField
    String name;
    @DatabaseField
    String facebookLink;
    @DatabaseField
    String email;
    String password;
    @DatabaseField
    String locationName;

    GeoPoint geoPoint;

    @DatabaseField
    double latitude = 0.0;

    @DatabaseField
    double longitude = 0.0;

    @DatabaseField
    String avatar;
    @DatabaseField
    String coverPicture;

    public User(){

    }

    public User(BackendlessUser backendlessUser){
        setId(backendlessUser.getUserId());
        setName((String) backendlessUser.getProperty(Constants.KEY_NAME));
        setEmail(backendlessUser.getEmail());
        setFacebookLink((String) backendlessUser.getProperty(Constants.KEY_FACEBOK_LINK));
        setlocation(Ulti.extractGeoPoint(backendlessUser));
        setLocationName((String) backendlessUser.getProperty(Constants.KEY_LOCATION_NAME));
        setAvatar((String) backendlessUser.getProperty(Constants.KEY_AVATAR));
        setCoverPicture((String) backendlessUser.getProperty(Constants.KEY_COVER_PICTURE));
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        setProperty(Constants.KEY_AVATAR, avatar);
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
        setProperty(Constants.KEY_COVER_PICTURE, coverPicture);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public GeoPoint getLocation() {
        if(geoPoint == null){
            geoPoint = new GeoPoint(latitude, longitude);
        }
        return geoPoint;
    }

    public void setlocation(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
        setProperty(Constants.KEY_LOCATION, geoPoint);
        this.latitude = geoPoint.getLatitude();
        this.longitude = geoPoint.getLongitude();
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
