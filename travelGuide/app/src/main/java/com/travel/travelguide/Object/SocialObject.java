package com.travel.travelguide.Object;

import com.github.gorbin.asne.instagram.InstagramSocialNetwork;
import com.github.gorbin.asne.twitter.TwitterSocialNetwork;

import vinasource.com.asnefacebook.FacebookSocialNetwork;

/**
 * Created by user on 5/15/16.
 */
public class SocialObject {
    public static final int FACEBOOK_TYPE = FacebookSocialNetwork.ID;
    public static final int TWITTER_TYPE = TwitterSocialNetwork.ID;
    public static final int INSTAGRAM_TYPE = InstagramSocialNetwork.ID;
    int id;
    String name;
    String link;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
