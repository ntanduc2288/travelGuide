package com.travel.travelguide.presenter.register;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by user on 4/22/16.
 */
public interface IRegisterView {
    void showLoading();
    void hideLoading();
    void invalidEmail();
    void invalidPassword();
    void invalidLocation();
    void showError(String error);
    void gotoMapScreen();
    void displayLocation(String location);
    void showAddSocialIconView();
    void hideAddSocialIconView();
    void initSocialPicker();
    LinearLayout getLayoutSocialContainer();
    Context getContext();
}
