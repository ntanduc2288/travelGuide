package com.travel.travelguide.fragment;

import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
    AppCompatEditText txtEmail;
    @Bind(R.id.btnResetPassword)
    ActionProcessButton btnResetPassword;
    @Bind(R.id.title)
    TextView lblTitle;
    @Bind(R.id.back_button)
    Button btnBack;

    ResetPasswordPresenter resetPasswordPresenter;
    private MaterialDialog dialog;

    public static ResetPasswordFragment newInstance(){
        return new ResetPasswordFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.forgot_password_fragment;
    }

    @Override
    protected void setupViews() {
        lblTitle.setText(getString(R.string.forgot_password));
        resetPasswordPresenter = new ResetPasswordPresenterImpl(this);
        btnResetPassword.setMode(ActionProcessButton.Mode.ENDLESS);
        btnResetPassword.setOnClickListener(this);
        btnBack.setOnClickListener(this);
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
        if(dialog == null){
            dialog = new MaterialDialog.Builder(getActivity())
                    .content(R.string.loading_three_dot)
                    .progress(true, 0)
                    .build();
        }

        dialog.show();
    }

    @Override
    public void hideLoading() {
        if(dialog != null){
            dialog.dismiss();
        }
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
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
        }
    }
}
