package com.travel.travelguide.Ulti;

import android.text.TextUtils;

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
}
