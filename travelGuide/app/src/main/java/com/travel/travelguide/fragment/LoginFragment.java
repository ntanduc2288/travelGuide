package com.travel.travelguide.fragment;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.travel.travelguide.R;
import com.travel.travelguide.manager.TransactionManager;
import com.travel.travelguide.presenter.Login.ILoginView;
import com.travel.travelguide.presenter.Login.LoginPresenter;
import com.travel.travelguide.presenter.Login.LoginPresenterImpl;

import butterknife.Bind;

/**
 * Created by user on 4/22/16.
 */
public class LoginFragment extends BaseFragment implements ILoginView, View.OnClickListener{
    @Bind(R.id.email)
    AutoCompleteTextView mEmailView;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.btnSignIn)
    ActionProcessButton btnActionSignIn;
    @Bind(R.id.btnRegister)
    ActionProcessButton btnActionRegister;
    @Bind(R.id.forgot_password)
    Button btnForgotPassword;


    private LoginPresenter loginPresenter;
    private final String TAG = LoginFragment.class.getSimpleName();

    public static LoginFragment newInstance(){
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_fragment;
    }

    @Override
    protected void setupViews() {
        loginPresenter = new LoginPresenterImpl(this);
        btnActionSignIn.setOnClickListener(this);
        btnActionSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        btnActionRegister.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
    }

    @Override
    public void showLoading() {
        btnActionSignIn.setProgress(50);
        btnActionSignIn.setEnabled(false);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void invalidEmail() {
        mEmailView.setError(getString(R.string.invalid_email));
        btnActionSignIn.setProgress(0);
        btnActionSignIn.setEnabled(true);
    }

    @Override
    public void invalidPassword() {
        mPasswordView.setError(getString(R.string.invalid_password));
        btnActionSignIn.setProgress(0);
        btnActionSignIn.setEnabled(true);
    }

    @Override
    public void showError(Integer errorCode) {
        btnActionSignIn.setProgress(0);
        btnActionSignIn.setEnabled(true);
        Toast.makeText(getActivity(), "Error code: " + errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String errorMessage) {
        btnActionSignIn.setProgress(0);
        btnActionSignIn.setEnabled(true);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void gotoMapScreen() {
        btnActionSignIn.setProgress(100);
        btnActionSignIn.setEnabled(true);
    }

    @Override
    public void gotoRegisterScreen() {
        TransactionManager.getInstance().addFragment(getFragmentManager(), RegisterFragment.newInstance());
    }

    @Override
    public void gotoForgotPasswordScreen() {
        TransactionManager.getInstance().addFragment(getFragmentManager(), ResetPasswordFragment.newInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignIn:

                loginPresenter.validateData(mEmailView.getText().toString().trim(), mPasswordView.getText().toString().trim());
                break;
            case R.id.btnRegister:
                gotoRegisterScreen();
                break;
            case R.id.forgot_password:
                gotoForgotPasswordScreen();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        loginPresenter.releaseResource();
        super.onDestroyView();
    }

}
