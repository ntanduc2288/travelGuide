package com.travel.travelguide.presenter.Login;

import com.travel.travelguide.presenter.IBasePresenter;

/**
 * Created by user on 4/22/16.
 */
public interface LoginPresenter extends IBasePresenter{
    void validateData(String email, String password);
    void login(String email, String password);
    void error(Integer errorCode);

}
