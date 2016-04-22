package com.travel.travelguide.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.presenter.register.IRegisterView;
import com.travel.travelguide.presenter.register.RegisterPresenter;
import com.travel.travelguide.presenter.register.RegisterPresenterImpl;

import butterknife.Bind;

/**
 * Created by user on 4/23/16.
 */
public class RegisterFragment extends BaseFragment implements IRegisterView, View.OnClickListener {
    @Bind(R.id.email)
    EditText mEmailView;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.btnRegister)
    ActionProcessButton btnActionRegister;
    @Bind(R.id.name) EditText txtName;
    @Bind(R.id.facebook) EditText txtFacebook;
    @Bind(R.id.confirm_password) EditText txtConfirmPassword;

    RegisterPresenter registerPresenter;
    public static RegisterFragment newInstance(){
        return new RegisterFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.register_fragment;
    }

    @Override
    protected void setupViews() {
        registerPresenter = new RegisterPresenterImpl(this);
        btnActionRegister.setOnClickListener(this);
        btnActionRegister.setMode(ActionProcessButton.Mode.ENDLESS);
    }

    @Override
    public void showLoading() {
        btnActionRegister.setProgress(50);
        btnActionRegister.setEnabled(false);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void invalidEmail() {
        mEmailView.setError(getString(R.string.invalid_email));
        btnActionRegister.setProgress(0);
        btnActionRegister.setEnabled(true);
    }

    @Override
    public void invalidPassword() {
        mPasswordView.setError(getString(R.string.invalid_password));
        btnActionRegister.setProgress(0);
        btnActionRegister.setEnabled(true);
    }

    @Override
    public void showError(Integer errorCode) {

    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
        btnActionRegister.setProgress(0);
        btnActionRegister.setEnabled(true);
    }

    @Override
    public void gotoMapScreen() {
        btnActionRegister.setProgress(100);
        btnActionRegister.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                User user = new User();
                user.setEmail(mEmailView.getText().toString().trim());
                user.setName(txtName.getText().toString().trim());
                user.setFacebookLink(txtFacebook.getText().toString().trim());
                registerPresenter.validateData(user, mPasswordView.getText().toString().trim(), txtConfirmPassword.getText().toString().trim());
                break;
        }
    }
}
