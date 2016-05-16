package com.travel.travelguide.presenter.profile;

import android.widget.LinearLayout;

import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.presenter.IBasePresenter;

import java.util.ArrayList;

/**
 * Created by user on 4/29/16.
 */
public interface ProfilePresenter extends IBasePresenter{
    void getUserProfile();
    void updateUserProfile();
    void updateUserProfile(String avatarFile);
    void setImageLocalPath(String imagePath);
    String getImageLocalPath();
    void switchMode();
    void error(Integer errorCode);
    void addMoreSocialView(LinearLayout lnContainer, SocialObject socialObject);
    ArrayList<SocialObject> getListSocialsRemainingItems();
    ArrayList<SocialObject> getListSocialsSelectedItems(LinearLayout lnContainer);

}
