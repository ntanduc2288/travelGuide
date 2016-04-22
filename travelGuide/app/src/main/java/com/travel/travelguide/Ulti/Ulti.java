package com.travel.travelguide.Ulti;

/**
 * Created by user on 4/22/16.
 */
public class Ulti {
    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
