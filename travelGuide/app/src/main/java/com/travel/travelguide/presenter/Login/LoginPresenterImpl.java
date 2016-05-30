package com.travel.travelguide.presenter.Login;

import android.content.Context;

import com.travel.travelguide.Ulti.GeneralCallback;
import com.travel.travelguide.Ulti.Ulti;
import com.travel.travelguide.manager.UserManager;
import com.travel.travelguide.presenter.BasePresenter;

/**
 * Created by user on 4/22/16.
 */
public class LoginPresenterImpl extends BasePresenter implements LoginPresenter {
    final String TAG = LoginPresenterImpl.class.getSimpleName();
    ILoginView loginView;
    Context context;

    public LoginPresenterImpl(Context context, ILoginView loginView) {
        this.context = context;
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

        UserManager.getInstance().signInBKUser(loginView.getContext(), email, password, new GeneralCallback(loginView.getContext()) {
            @Override
            public void success(Object o) {
                if(viewIsValid()){
                    loginView.hideLoading();
                    loginView.gotoMapScreen();
                }
            }

            @Override
            public void error(String errorMessage) {
                super.error(errorMessage);
                if(viewIsValid()){
                    loginView.hideLoading();
                }
            }
        });

    }

    @Override
    public void error(Integer errorCode) {
        loginView.hideLoading();
        loginView.showError(errorCode);
    }


    @Override
    public void releaseResources() {
        releaseDatabaseHelper();
        loginView = null;
    }

    @Override
    public boolean viewIsValid() {
        if(loginView != null) return true;
        return false;
    }


}
