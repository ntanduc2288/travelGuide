package com.travel.travelguide.presenter.resetPassword;

/**
 * Created by user on 4/22/16.
 */
public interface IResetPasswordView {
    void showLoading();
    void hideLoading();
    void invalidEmail();
    void showMessage(String message);
}
