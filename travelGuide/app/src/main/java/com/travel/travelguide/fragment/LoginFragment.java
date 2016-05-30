package com.travel.travelguide.fragment;

import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.iml.ActionProcessButton;
import com.travel.travelguide.R;
import com.travel.travelguide.activity.MainActivity;
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
    AppCompatEditText txtEmail;
    @Bind(R.id.password)
    AppCompatEditText txtPassword;
    @Bind(R.id.btnSignIn)
    ActionProcessButton btnActionSignIn;
    @Bind(R.id.btnRegister)
    ActionProcessButton btnActionRegister;
    @Bind(R.id.forgot_password)
    Button btnForgotPassword;
    MaterialDialog dialog;

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
        loginPresenter = new LoginPresenterImpl(getActivity().getApplicationContext(),this);
        btnActionSignIn.setOnClickListener(this);
        btnActionSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        btnActionRegister.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
    }

    @Override
    public void showLoading() {
        btnActionSignIn.setProgress(50);
        shouldEnableButtons(false);
        if(dialog == null){
            dialog = new MaterialDialog.Builder(getActivity())
                    .content(R.string.waiting_dot)
                    .backgroundColor(getResources().getColor(R.color.transparent))
                    .progress(true, 0)
                    .build();
        }

        dialog.show();
    }

    @Override
    public void hideLoading() {
        shouldEnableButtons(true);
        if(dialog != null){
            dialog.dismiss();
        }
    }

    @Override
    public void invalidEmail() {
        txtEmail.setError(getString(R.string.invalid_email));
        btnActionSignIn.setProgress(0);

    }

    @Override
    public void invalidPassword() {
        txtPassword.setError(getString(R.string.invalid_password));
        btnActionSignIn.setProgress(0);
    }

    @Override
    public void showError(Integer errorCode) {
        btnActionSignIn.setProgress(0);
        Toast.makeText(getActivity(), "Error code: " + errorCode, Toast.LENGTH_SHORT).show();
    }

    private void shouldEnableButtons(boolean should){
        btnActionSignIn.setEnabled(should);
        btnActionRegister.setEnabled(should);
        btnForgotPassword.setEnabled(should);
    }

    @Override
    public void gotoMapScreen() {
        btnActionSignIn.setProgress(100);
        TransactionManager.getInstance().gotoActivity(getActivity(), MainActivity.class, null, true);
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

                loginPresenter.validateData(txtEmail.getText().toString().trim(), txtPassword.getText().toString().trim());
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
        loginPresenter.releaseResources();
        super.onDestroyView();
    }

}
