package com.travel.travelguide.presenter.Login;

/**
 * Created by user on 4/22/16.
 */
public interface LoginPresenter {
    void validateData(String email, String password);
    void login(String email, String password);
    void error(Integer errorCode);
    void releaseResource();
}
