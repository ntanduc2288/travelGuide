package com.travel.travelguide.presenter.Login;

/**
 * Created by user on 4/22/16.
 */
public interface ILoginView {
    void showLoading();
    void hideLoading();
    void invalidEmail();
    void invalidPassword();
    void showError(Integer errorCode);
    void showError(String errorMessage);
    void gotoMapScreen();
    void gotoRegisterScreen();
    void gotoForgotPasswordScreen();
}
