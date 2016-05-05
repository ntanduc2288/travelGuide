package com.travel.travelguide.presenter.profile;

import com.travel.travelguide.Object.User;
import com.travel.travelguide.manager.UserManager;

/**
 * Created by user on 4/29/16.
 */
public class ProfilePresenterImpl implements ProfilePresenter {
    IProfileView profileView;
    User user;
    boolean isMyProfileView;
    boolean isInEditMode;

    public ProfilePresenterImpl(IProfileView profileView, User user) {
        this.profileView = profileView;
        this.user = user;
        isMyProfileView = user.getId().equalsIgnoreCase(UserManager.getInstance().getCurrentUser().getId());
    }

    @Override
    public void getUserProfile() {
        profileView.showLoading();
        profileView.bindData(user);
        if(isMyProfileView){
            profileView.showMyProfileViews();
        }else {
            profileView.showUserProfileViews();
        }
        profileView.switchToViewerMode();
        profileView.hideLoading();
    }

    @Override
    public void switchMode() {
        if(isInEditMode){
            //Save data and switch to viewer mode
            profileView.switchToViewerMode();
        }else {
            profileView.switchToEditMode();
        }
        isInEditMode = !isInEditMode;
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
    public void logout() {
        profileView.showLoading();
        boolean result = UserManager.getInstance().clearCurrentUserInfo(profileView.getContext().getApplicationContext());
        profileView.hideLoading();
        if(result){
            profileView.gotoLoginScreen();
        }else {
            profileView.showMessage("Could not logout");
        }


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
