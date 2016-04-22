package com.travel.travelguide.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.travel.travelguide.R;
import com.travel.travelguide.presenter.resetPassword.IResetPasswordView;
import com.travel.travelguide.presenter.resetPassword.ResetPasswordPresenter;
import com.travel.travelguide.presenter.resetPassword.ResetPasswordPresenterImpl;

import butterknife.Bind;

/**
 * Created by user on 4/23/16.
 */
public class ResetPasswordFragment extends BaseFragment implements IResetPasswordView, View.OnClickListener{
    @Bind(R.id.email)
    EditText txtEmail;
    @Bind(R.id.btnResetPassword)
    ActionProcessButton btnResetPassword;

    ResetPasswordPresenter resetPasswordPresenter;

    public static ResetPasswordFragment newInstance(){
        return new ResetPasswordFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.forgot_password_fragment;
    }

    @Override
    protected void setupViews() {
        resetPasswordPresenter = new ResetPasswordPresenterImpl(this);
        btnResetPassword.setMode(ActionProcessButton.Mode.ENDLESS);
        btnResetPassword.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        resetPasswordPresenter.cleanResource();
        resetPasswordPresenter = null;
        super.onDestroyView();

    }

    @Override
    public void showLoading() {
        btnResetPassword.setProgress(50);
        btnResetPassword.setEnabled(false);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void invalidEmail() {
        txtEmail.setError(getString(R.string.invalid_email));
        btnResetPassword.setProgress(0);
        btnResetPassword.setEnabled(true);
    }

    @Override
    public void showMessage(String message) {
        btnResetPassword.setProgress(0);
        btnResetPassword.setEnabled(true);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnResetPassword:
                resetPasswordPresenter.validateData(txtEmail.getText().toString().trim());
                break;
        }
    }
}
