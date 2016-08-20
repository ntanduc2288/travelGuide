package com.travel.travelguide.presenter.editProfile;

import com.travel.travelguide.Object.User;

import android.content.Context;
import android.widget.LinearLayout;

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
    void gotoLoginScreen();
    LinearLayout getLayoutSocialContainer();
}
