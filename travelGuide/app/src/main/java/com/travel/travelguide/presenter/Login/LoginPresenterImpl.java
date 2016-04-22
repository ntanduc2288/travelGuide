package com.travel.travelguide.presenter.Login;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.travel.travelguide.Ulti.LogUtils;
import com.travel.travelguide.Ulti.Ulti;

/**
 * Created by user on 4/22/16.
 */
public class LoginPresenterImpl implements LoginPresenter {
    final String TAG = LoginPresenterImpl.class.getSimpleName();
    ILoginView loginView;

    public LoginPresenterImpl(ILoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void validateData(String email, String password) {
        loginView.showLoading();
        if(!Ulti.isEmailValid(email)){
            loginView.hideLoading();
            loginView.invalidEmail();
        }else if (password.length() < 3){
            loginView.hideLoading();
            loginView.invalidPassword();
        }else {
            login(email, password);
        }
    }

    @Override
    public void login(String email, String password) {

        Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                LogUtils.logD(TAG, "handle response login: " + response.toString());
                loginView.hideLoading();
                loginView.gotoMapScreen();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                LogUtils.logD(TAG, "handleFault login: " + fault.getMessage());
                loginView.hideLoading();
                loginView.showError(fault.getMessage());
            }
        });

    }

    @Override
    public void error(Integer errorCode) {
        loginView.hideLoading();
        loginView.showError(errorCode);
    }

    @Override
    public void releaseResource() {
        loginView = null;
    }
}
