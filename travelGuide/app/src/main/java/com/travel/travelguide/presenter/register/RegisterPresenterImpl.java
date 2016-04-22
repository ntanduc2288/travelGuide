package com.travel.travelguide.presenter.register;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.Ulti.LogUtils;
import com.travel.travelguide.Ulti.Ulti;

/**
 * Created by user on 4/23/16.
 */
public class RegisterPresenterImpl implements RegisterPresenter {

    IRegisterView registerView;
    private final String TAG = RegisterPresenterImpl.class.getSimpleName();

    public RegisterPresenterImpl(IRegisterView registerView) {
        this.registerView = registerView;
    }

    @Override
    public void validateData(User user, String password, String confirmPassword) {

        registerView.showLoading();
        if(!Ulti.isEmailValid(user.getEmail())){
            registerView.hideLoading();
            registerView.invalidEmail();
        }else if (!password.equals(confirmPassword) || password.length() < 3){
            registerView.hideLoading();
            registerView.invalidPassword();
        }else {
            user.setPassword(password);
            register(user);
        }

    }

    @Override
    public void register(User user) {
        Backendless.UserService.register(user, new BackendlessCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                    LogUtils.logD(TAG, response.toString());
                    User userTmp = new User(response);
                    registerView.hideLoading();
                    registerView.gotoMapScreen();

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                registerView.hideLoading();
                registerView.showError(fault.getMessage());
            }
        });
    }

    @Override
    public void error(Integer errorCode) {

    }

    @Override
    public void releaseResources() {
        registerView = null;
    }
}
