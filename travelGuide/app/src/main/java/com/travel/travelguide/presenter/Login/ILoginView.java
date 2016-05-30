package com.travel.travelguide.presenter.Login;

import android.content.Context;

/**
 * Created by user on 4/22/16.
 */
public interface ILoginView {
    void showLoading();
    void hideLoading();
    void invalidEmail();
    void invalidPassword();
    void showError(Integer errorCode);
    void gotoMapScreen();
    void gotoRegisterScreen();
    void gotoForgotPasswordScreen();
    Context getContext();
}
