package com.travel.travelguide.presenter.register;

/**
 * Created by user on 4/22/16.
 */
public interface IRegisterView {
    void showLoading();
    void hideLoading();
    void invalidEmail();
    void invalidPassword();
    void showError(Integer errorCode);
    void showError(String errorMessage);
    void gotoMapScreen();
}
