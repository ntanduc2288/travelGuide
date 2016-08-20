package com.travel.travelguide.Object;

import com.backendless.BackendlessUser;
import com.backendless.geo.GeoPoint;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.Ulti.LogUtils;
import com.travel.travelguide.Ulti.Ulti;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 4/23/16.
 */
@DatabaseTable(tableName = "User")
public class User extends BackendlessUser {
    @DatabaseField(id = true)
    String backendlessUserId = Constants.EMPTY_STRING;
    @DatabaseField
    String name = Constants.EMPTY_STRING;
    @DatabaseField
    String facebookLink = Constants.EMPTY_STRING;
    @DatabaseField
    String email = Constants.EMPTY_STRING;
    String password = Constants.EMPTY_STRING;
    @DatabaseField
    String locationName = Constants.EMPTY_STRING;
    GeoPoint geoPoint;
    @DatabaseField
    double latitude = 0.0;
    @DatabaseField
    double longitude = 0.0;
    @DatabaseField
    String avatar = Constants.EMPTY_STRING;
    @DatabaseField
    String coverPicture = Constants.EMPTY_STRING;

    @DatabaseField
    String phoneNumber = Constants.EMPTY_STRING;
    @DatabaseField
    String twitterLink = Constants.EMPTY_STRING;
    @DatabaseField
    String instagramLink = Constants.EMPTY_STRING;
    @DatabaseField
    String language = Constants.EMPTY_STRING;
    @DatabaseField
    long travelDateFrom = 0;
    @DatabaseField
    long travelDateTo = 0;

    @DatabaseField
    String aboutMe = Constants.EMPTY_STRING;

    @DatabaseField
    String interest = Constants.EMPTY_STRING;

    @DatabaseField
    int qbUserId = 0;

    @DatabaseField
    int numberOfPeople = 0;

    @DatabaseField
    String destination = Constants.EMPTY_STRING;


    public User(){

    }

    public User(BackendlessUser backendlessUser){
        setbackendlessUserId(backendlessUser.getUserId());
        setName((String) backendlessUser.getProperty(Constants.KEY_NAME));
        setEmail(backendlessUser.getEmail());
        setFacebookLink((String) backendlessUser.getProperty(Constants.KEY_FACEBOK_LINK));
        if(backendlessUser.getProperty(Constants.KEY_LOCATION) != null){
            setlocation(Ulti.extractGeoPoint(backendlessUser));
        }
        setLocationName((String) backendlessUser.getProperty(Constants.KEY_LOCATION_NAME));
        setAvatar((String) backendlessUser.getProperty(Constants.KEY_AVATAR));
        setCoverPicture((String) backendlessUser.getProperty(Constants.KEY_COVER_PICTURE));
        setPhoneNumber((String) backendlessUser.getProperty(Constants.KEY_PHONE_NUMBER));
        setTwitterLink((String) backendlessUser.getProperty(Constants.KEY_TWITTER_LINK));
        setInstagramLink((String) backendlessUser.getProperty(Constants.KEY_INSTAGRAM_LINK));
        setLanguage((String) backendlessUser.getProperty(Constants.KEY_LANGUAGE));
        setAboutMe((String) backendlessUser.getProperty(Constants.KEY_ABOUT_ME));
        setInterest((String) backendlessUser.getProperty(Constants.KEY_INTEREST));
        try {
            Date startDate = (Date) backendlessUser.getProperty(Constants.KEY_TRAVEL_DATE_FROM);
            Date endDate = (Date) backendlessUser.getProperty(Constants.KEY_TRAVEL_DATE_TO);
            setTravelDateFrom(startDate.getTime());
            setTravelDateTo(endDate.getTime());
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.logE(User.class.getSimpleName(), e.toString());
        }
        setQbUserId((Integer) backendlessUser.getProperty(Constants.KEY_QBLOX_USER_ID));
        setNumberOfPeople((Integer) backendlessUser.getProperty(Constants.KEY_NUMBER_OF_PEOPLE));
        setDestination((String) backendlessUser.getProperty(Constants.KEY_DESTINATION));
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
        setProperty(Constants.KEY_NUMBER_OF_PEOPLE, numberOfPeople);
    }

    public String getDestination(){
        return destination;
    }

    public void setDestination(String destination){
        this.destination = destination;
        setProperty(Constants.KEY_DESTINATION, destination);
    }

    public int getQbUserId() {
        return qbUserId;
    }

    public void setQbUserId(int qbUserId) {
        this.qbUserId = qbUserId;
        setProperty(Constants.KEY_QBLOX_USER_ID, qbUserId);
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
        setProperty(Constants.KEY_INTEREST, interest);
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
        setProperty(Constants.KEY_ABOUT_ME, aboutMe);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        setProperty(Constants.KEY_PHONE_NUMBER, phoneNumber);
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
        setProperty(Constants.KEY_TWITTER_LINK, twitterLink );
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
        setProperty(Constants.KEY_INSTAGRAM_LINK, instagramLink );
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        setProperty(Constants.KEY_LANGUAGE, language );
    }

    public long getTravelDateFrom() {
        return travelDateFrom;
    }

    public void setTravelDateFrom(long travelDateFrom) {
        this.travelDateFrom = travelDateFrom;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(travelDateFrom);
        setProperty(Constants.KEY_TRAVEL_DATE_FROM, calendar.getTime() );
    }

    public long getTravelDateTo() {
        return travelDateTo;
    }

    public void setTravelDateTo(long travelDateTo) {
        this.travelDateTo = travelDateTo;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(travelDateTo);
        setProperty(Constants.KEY_TRAVEL_DATE_TO, calendar.getTime());
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

    public String getbackendlessUserId() {
        return backendlessUserId;
    }

    public void setbackendlessUserId(String id) {
        this.backendlessUserId = id;
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

    public User(User other) {
        setbackendlessUserId(other.backendlessUserId);
        setName(other.name);
        setFacebookLink(other.facebookLink);
        setEmail(other.email);
        setLocationName(other.locationName);
        setLatitude(other.latitude);
        setLongitude(other.longitude);
        setAvatar(other.avatar);
        setCoverPicture(other.coverPicture);
        setPhoneNumber(other.phoneNumber);
        setTwitterLink(other.twitterLink);
        setInstagramLink(other.instagramLink);
        setLanguage(other.language);
        setTravelDateFrom(other.travelDateFrom);
        setTravelDateTo(other.travelDateTo);
        setAboutMe(other.aboutMe);
        setInterest(other.interest);
        setQbUserId(other.qbUserId);
        setNumberOfPeople(other.numberOfPeople);
        setDestination(other.destination);
    }
}
