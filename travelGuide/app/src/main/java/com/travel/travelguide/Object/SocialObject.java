package com.travel.travelguide.Object;

/**
 * Created by user on 5/15/16.
 */
public class SocialObject {
    public static final int FACEBOOK_TYPE = 0;
    public static final int TWITTER_TYPE = 1;
    public static final int INSTAGRAM_TYPE = 2;
    int id;
    String name;

    public SocialObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
