package com.travel.travelguide.presenter.resetPassword;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.travel.travelguide.Ulti.Ulti;

/**
 * Created by user on 4/23/16.
 */
public class ResetPasswordPresenterImpl implements ResetPasswordPresenter {
    IResetPasswordView resetPasswordView;

    public ResetPasswordPresenterImpl(IResetPasswordView resetPasswordView) {
        this.resetPasswordView = resetPasswordView;
    }

    @Override
    public void validateData(String email) {
        resetPasswordView.showLoading();
        if(!Ulti.isEmailValid(email)){
            resetPasswordView.hideLoading();
            resetPasswordView.invalidEmail();
        }else{
            resetPassword(email);
        }

    }

    @Override
    public void resetPassword(String email) {
        Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                resetPasswordView.hideLoading();
                resetPasswordView.showMessage("Successful");
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                resetPasswordView.hideLoading();
                resetPasswordView.showMessage(fault.getMessage());
            }
        });
    }

    @Override
    public void cleanResource() {
        resetPasswordView = null;
    }
}
