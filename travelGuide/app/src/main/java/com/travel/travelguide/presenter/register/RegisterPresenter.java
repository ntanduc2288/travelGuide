package com.travel.travelguide.presenter.register;

import com.travel.travelguide.Object.User;
import com.travel.travelguide.presenter.IBasePresenter;

/**
 * Created by user on 4/23/16.
 */
public interface RegisterPresenter extends IBasePresenter{
    void validateData(User user, String password, String confirmPassword);
    void register(User user);
    void error(Integer errorCode);
}
