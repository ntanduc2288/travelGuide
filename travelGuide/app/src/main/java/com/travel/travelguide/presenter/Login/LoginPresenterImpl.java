package com.travel.travelguide.presenter.Login;

import android.content.Context;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.Ulti.LogUtils;
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

        Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                LogUtils.logD(TAG, "handle response login: " + response.toString());
                UserManager.getInstance().setCurrentUser(new User(response));
                UserManager.getInstance().saveUserToDatabase(context);
                if(viewIsValid()){
                    loginView.hideLoading();
                    loginView.gotoMapScreen();
                }

//                response.setProperty("avatar", "Link " + Calendar.getInstance().getTimeInMillis());
//                UserManager.getInstance().setCurrentUser(new User(response));
//                Backendless.UserService.update(UserManager.getInstance().getCurrentUser(), new AsyncCallback<BackendlessUser>() {
//                    @Override
//                    public void handleResponse(BackendlessUser response) {
//                        UserManager.getInstance().setCurrentUser(new User(response));
//                        UserManager.getInstance().saveUserToDatabase(context);
//                        if (viewIsValid()) {
//                            loginView.hideLoadindMarkerProcess();
//                            loginView.gotoMapScreen();
//                        }
//                    }
//
//                    @Override
//                    public void handleFault(BackendlessFault fault) {
//                        LogUtils.logD(TAG, "handleFault login: " + fault.getMessage());
//                        if(viewIsValid()){
//                            loginView.hideLoadindMarkerProcess();
//                            loginView.showError(fault.getMessage());
//                        }
//                    }
//                });
            }


            @Override
            public void handleFault(BackendlessFault fault) {
                LogUtils.logD(TAG, "handleFault login: " + fault.getMessage());
                if(viewIsValid()){
                    loginView.hideLoading();
                    loginView.showError(fault.getMessage());
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
