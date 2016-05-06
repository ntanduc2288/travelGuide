package com.travel.travelguide.presenter.register;

import android.text.TextUtils;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.Ulti.LogUtils;
import com.travel.travelguide.Ulti.Ulti;
import com.travel.travelguide.manager.UserManager;

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

        if(viewIsValid()){
            registerView.showLoading();
            if(!Ulti.isEmailValid(user.getEmail())){
                registerView.hideLoading();
                registerView.invalidEmail();
            }else if (!password.equals(confirmPassword) || password.length() < 3){
                registerView.hideLoading();
                registerView.invalidPassword();
            }else if (TextUtils.isEmpty(user.getLocationName())){
                registerView.hideLoading();
                registerView.invalidLocation();
            }else {
                user.setPassword(password);
                register(user);
            }
        }

    }

    @Override
    public void register(User user) {
        Backendless.UserService.register(user, new BackendlessCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                    LogUtils.logD(TAG, response.toString());
                    User userTmp = new User(response);
                    UserManager.getInstance().setCurrentUser(userTmp);
                     UserManager.getInstance().saveUserToDatabase(registerView.getContext());
                    if(viewIsValid()){
                        registerView.hideLoading();
                        registerView.gotoMapScreen();
                    }

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(viewIsValid()){
                    registerView.hideLoading();
                    registerView.showError(fault.getMessage());
                }
            }
        });
    }

    @Override
    public void error(Integer errorCode) {

    }

    @Override
    public boolean viewIsValid(){
        if(registerView != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void releaseResources() {
        registerView = null;
    }
}
