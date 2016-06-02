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
    public static final String KEY_TRAVEL_DATE_FROM = "travelDateFrom";
    public static final String KEY_TRAVEL_DATE_TO = "travelDateTo";
    public static final String KEY_ABOUT_ME = "aboutMe";
    public static final String KEY_INTEREST = "interest";
    public static final String KEY_QBLOX_USER_ID = "QBloxUserId";

    public static final float DEFAULT_RADIUS = 20; //mile
    public static final float DEFAULT_ZOOM_LEVEL = 14;
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
}
