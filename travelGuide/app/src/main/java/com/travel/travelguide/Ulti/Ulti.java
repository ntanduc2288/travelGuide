package com.travel.travelguide.Ulti;

import android.content.Context;
import android.text.TextUtils;

import com.backendless.BackendlessUser;
import com.backendless.geo.GeoPoint;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.instagram.InstagramPerson;
import com.github.gorbin.asne.twitter.TwitterPerson;
import com.travel.travelguide.Object.Languages;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.manager.UserManager;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.Map;

import vinasource.com.asnefacebook.FacebookPerson;

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

    public static String[] parseLanguage(Context context){
        String[] languages = null;

        Serializer serializer = new Persister();
        try {
            Languages languages1 = serializer.read(Languages.class, context.getApplicationContext().getAssets().open("languages.xml"));
            languages = new String[languages1.getList().size()];
            for (int i = 0; i < languages1.getList().size(); i++) {
                languages[i] = languages1.getList().get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(languages == null){
            languages = new String[]{"English"};
        }

        return languages;
    }

    public static String getSocialLink(SocialPerson socialPerson){
        String result;

        if(socialPerson instanceof FacebookPerson){
            result = "https://www.facebook.com/" + socialPerson.id;
        }else if (socialPerson instanceof TwitterPerson){
            result = "https://twitter.com/" + ((TwitterPerson) socialPerson).screenName;
        }else if (socialPerson instanceof InstagramPerson){
            result = socialPerson.profileURL;
        }else {
            result = socialPerson.name;
        }

        return result;
    }

    public static String getDefaultMessageContent(){
        User user = UserManager.getInstance().getCurrentUser();
        String travelFrom = DateHelper.convertLongMilisecondsToDateString(user.getTravelDateFrom(), DateHelper.RFC_USA_7);
        String travelTo = DateHelper.convertLongMilisecondsToDateString(user.getTravelDateTo(), DateHelper.RFC_USA_7);
        String defaultMessage = "Hi %s, my name is " + user.getName() + ", i am plainning " +
                "to visit " + user.getDestination() + " from " + travelFrom + " to " + travelTo + "."
                + "Would you be interested to be my host?";

        return defaultMessage;
    }



}
