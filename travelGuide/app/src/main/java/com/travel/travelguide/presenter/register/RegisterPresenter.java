package com.travel.travelguide.presenter.register;

import android.widget.LinearLayout;

import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.presenter.IBasePresenter;

import java.util.ArrayList;

/**
 * Created by user on 4/23/16.
 */
public interface RegisterPresenter extends IBasePresenter{
    void validateData(User user, String password, String confirmPassword);
    void register(User user);
    void error(Integer errorCode);
    void addMoreSocialView(LinearLayout lnContainer, SocialObject socialObject);
    ArrayList<SocialObject> getListSocialsRemainingItems();
    ArrayList<SocialObject> getListSocialsSelectedItems(LinearLayout lnContainer);

}
