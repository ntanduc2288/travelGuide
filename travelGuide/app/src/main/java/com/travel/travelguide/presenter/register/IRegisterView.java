package com.travel.travelguide.presenter.register;

import android.content.Context;

/**
 * Created by user on 4/22/16.
 */
public interface IRegisterView {
    void showLoading();
    void hideLoading();
    void invalidEmail();
    void invalidPassword();
    void invalidLocation();
    void showError(Integer errorCode);
    void gotoMapScreen();
    void displayLocation(String location);
    void showAddSocialButton();
    void hideAddSocialButton();
    Context getContext();
}
