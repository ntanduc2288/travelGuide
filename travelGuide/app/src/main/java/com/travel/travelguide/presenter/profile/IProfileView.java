package com.travel.travelguide.presenter.profile;

import android.content.Context;

import com.travel.travelguide.Object.User;

/**
 * Created by user on 4/29/16.
 */
public interface IProfileView {
    void showLoading();
    void hideLoading();
    void bindData(User user);
    void showMyProfileViews();
    void showUserProfileViews();
    void switchToEditMode();
    void switchToViewerMode();
    void gotoLoginScreen();
    void showMessage(String message);
    Context getContext();
}
