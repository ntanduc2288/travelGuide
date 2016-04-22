package com.travel.travelguide.presenter.resetPassword;

/**
 * Created by user on 4/23/16.
 */
public interface ResetPasswordPresenter {
    void validateData(String email);
    void resetPassword(String email);
    void cleanResource();
}
