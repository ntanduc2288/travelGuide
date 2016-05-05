package com.travel.travelguide.presenter.profile;

import com.travel.travelguide.presenter.IBasePresenter;

/**
 * Created by user on 4/29/16.
 */
public interface ProfilePresenter extends IBasePresenter{
    void getUserProfile();
    void updateUserProfile();
    void switchMode();
    void logout();
    void error(Integer errorCode);
}
