package com.travel.travelguide.fragment;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.presenter.profile.IProfileView;
import com.travel.travelguide.presenter.profile.ProfilePresenter;
import com.travel.travelguide.presenter.profile.ProfilePresenterImpl;

import butterknife.Bind;

/**
 * Created by user on 4/29/16.
 */
public class ProfileFragment extends BaseFragment implements IProfileView, View.OnClickListener{

    @Bind(R.id.title)
    AppCompatTextView lblTitle;
    @Bind(R.id.back_button)
    AppCompatButton btnBack;
    User user;
    ProfilePresenter profilePresenter;

    public static ProfileFragment newInstance(User user){
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setUser(user);
        return profileFragment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.profile_screen;
    }

    @Override
    protected void setupViews() {
        profilePresenter = new ProfilePresenterImpl(this, user);
        profilePresenter.getUserProfile();
        btnBack.setOnClickListener(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void bindData(User user) {
        lblTitle.setText(user.getName());
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
        }
    }
}
