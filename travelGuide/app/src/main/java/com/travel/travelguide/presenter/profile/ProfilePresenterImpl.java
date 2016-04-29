package com.travel.travelguide.presenter.profile;

import com.travel.travelguide.Object.User;

/**
 * Created by user on 4/29/16.
 */
public class ProfilePresenterImpl implements ProfilePresenter {
    IProfileView profileView;
    User user;

    public ProfilePresenterImpl(IProfileView profileView, User user) {
        this.profileView = profileView;
        this.user = user;
    }

    @Override
    public void getUserProfile() {
        profileView.showLoading();
        profileView.bindData(user);
        profileView.hideLoading();
    }

    @Override
    public void updateUserProfile() {
        profileView.showLoading();
    }

    @Override
    public void error(Integer errorCode) {
        profileView.hideLoading();
        profileView.showMessage("Testing");
    }

    @Override
    public void releaseResources() {
        profileView = null;
    }

    @Override
    public boolean viewIsValid() {
        if(profileView != null){
            return true;
        }else {
            return false;
        }
    }
}
