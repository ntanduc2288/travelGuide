package com.travel.travelguide.manager;

import com.travel.travelguide.Object.User;

/**
 * Created by user on 4/23/16.
 */
public class UserManager {
    private static UserManager instance;
    User user;
    public static UserManager getInstance(){
        if(instance == null){
            synchronized (UserManager.class){
                if(instance == null){
                    instance = new UserManager();
                }
            }
        }

        return instance;
    }

    private UserManager(){}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
