package com.travel.travelguide.presenter.editProfile;

import android.content.Context;

import com.travel.travelguide.Object.User;

/**
 * Created by user on 4/29/16.
 */
public interface IEditProfileView {
    void showLoading();
    void hideLoading();
    void bindData(User user);
    void showMyProfileViews();
    void showUserProfileViews();
    void switchToEditMode();
    void switchToViewerMode();
    void showMessage(String message);
    Context getContext();
    void showAddSocialButton();
    void hideAddSocialButton();
    void updateUserInfoSuccessfull(User user);
}
