package com.travel.travelguide.Ulti;

import android.os.Environment;

/**
 * Created by user on 4/23/16.
 */
public class Constants {

    public static final String KEY_NAME = "name";
    public static final String KEY_FACEBOK_LINK = "facebookLink";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_LOCATION_NAME = "locationName";
    public static final String KEY_LOCATION = "locations";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_COVER_PICTURE = "coverPicture";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_TWITTER_LINK = "twitterLink";
    public static final String KEY_INSTAGRAM_LINK = "instagramLink";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_TRAVEL_DATE_FROM = "startItiniraryDate";
    public static final String KEY_TRAVEL_DATE_TO = "endItineraryDate";
    public static final String KEY_ABOUT_ME = "aboutMe";
    public static final String KEY_INTEREST = "interest";
    public static final String KEY_QBLOX_USER_ID = "QBloxUserId";
    public static final String KEY_NUMBER_OF_PEOPLE = "numberOfPeople";
    public static final String KEY_DESTINATION = "destination";

    public static final float DEFAULT_RADIUS = 20; //mile
    public static final float DEFAULT_ZOOM_LEVEL = 14;
    public static final float COUNTRY_ZOOM_LEVEL = 5;
    public static final String EMPTY_STRING = "";

    public static final String RETURN_DATA = "return-data";
    public static final String imagePath = Environment.getExternalStorageDirectory().getPath() + "/tmpImage.jpg";
    public static final int REQUEST_SELECT_PICTURE = 1;
    public static final int CAMERA_CODE = 2;
    public static final int GALLERY_CODE = 3;
    public static final int CROP_CODE = 4;
    public static final int PLACE_PICKER_REQUEST = 5;
    public static final int MAX_IMAGE_SIZE = 700;
    public static final String IMAGE_FOLDER_SERVER = "media/images";

    public static final String DEFAULT_LANGUAGE = "English";
    public static final String UNAUTHORIZE_ERROR = "Unauthorized";

    //Keys
    public static final String TWITTER_CONSUMER_KEY = "TWITTER_CONSUMER_KEY";
    public static final String TWITTER_CONSUMER_SECRET = "TWITTER_CONSUMER_SECRET";
    public static final String LINKEDIN_CONSUMER_KEY = "LINKEDIN_CONSUMER_KEY";
    public static final String LINKEDIN_CONSUMER_SECRET = "LINKEDIN_CONSUMER_SECRET";
    public static final String VK_KEY =  "VK_KEY";
    public static final String OK_APP_ID =  "OK_APP_ID";
    public static final String OK_PUBLIC_KEY =  "OK_PUBLIC_KEY";
    public static final String OK_SECRET_KEY =  "OK_SECRET_KEY";
    public static final String INSTAGRAM_CLIENT_KEY = "INSTAGRAM_KEY";
    public static final String INSTAGRAM_CLIENT_SECRET = "INSTAGRAM_SECRET";

    //redirect urls
    public static final String TWITTER_CALLBACK_URL = "http://github.com/gorbin/ASNE";
    public static final String LINKEDIN_CALLBACK_URL = "https://asne";
    public static final String INSTAGRAM_CALLBACK_URL = "oauth://ASNE";

    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";
}
