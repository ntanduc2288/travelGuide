package com.travel.travelguide.presenter.register;

import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.presenter.IBasePresenter;

import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by user on 4/23/16.
 */
public interface RegisterPresenter extends IBasePresenter{
    void validateData(User user, String password, String confirmPassword);
    void register(User user);
    void error(Integer errorCode);
    ArrayList<SocialObject> getListSocialsRemainingItems();
    ArrayList<SocialObject> getListSocialsSelectedItems(LinearLayout lnContainer);
    public void setSocialNetworkManager(SocialNetworkManager socialNetworkManager);
    void addMoreSocialView(int socialNetworkID, SocialPerson socialPerson);
    public void getSocialInfo(SocialObject socialObject);

}
