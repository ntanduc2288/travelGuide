package com.travel.travelguide.presenter.editProfile;

import com.github.gorbin.asne.core.persons.SocialPerson;
import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.presenter.IBasePresenter;

import android.widget.LinearLayout;

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
    ArrayList<SocialObject> getListSocialsRemainingItems();
    ArrayList<SocialObject> getListSocialsSelectedItems(LinearLayout lnContainer);
    void addMoreSocialView(int socialNetworkID, SocialPerson socialPerson);
    void addMoreSocialView(SocialObject socialObject);
    public void getSocialInfo(SocialObject socialObject);

}
