package com.travel.travelguide.fragment;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.travel.travelguide.R;
import com.travel.travelguide.activity.LoginActivity;
import com.travel.travelguide.manager.TransactionManager;
import com.travel.travelguide.manager.UserManager;
import com.travel.travelguide.presenter.settings.SettingsFragmentPresenter;
import com.travel.travelguide.presenter.settings.SettingsFragmentPresenterImpl;
import com.travel.travelguide.presenter.settings.SettingsView;

import butterknife.Bind;

/**
 * Created by user on 5/27/16.
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener, SettingsView{
    @Bind(R.id.textview_user_name)
    AppCompatTextView lblUserName;
    @Bind(R.id.button_edit_personal_info)
    AppCompatButton btnEditPersonalInfo;
    @Bind(R.id.button_message)
    AppCompatButton btnMessage;
    @Bind(R.id.button_add_edit_travel_itinerary)
    AppCompatButton btnAddEditTravelItinerary;
    @Bind(R.id.button_invite_friends)
    AppCompatButton btnInviteFriend;
    @Bind(R.id.button_logout)
    AppCompatButton btnLogout;
    @Bind(R.id.title)
    TextView lblTitle;
    @Bind(R.id.back_button)
    AppCompatButton btnBack;

    MaterialDialog dialog;
    SettingsFragmentPresenter settingsFragmentPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.settings_fragment;
    }

    @Override
    protected void setupViews() {
        lblUserName.setText(UserManager.getInstance().getCurrentUser().getName());
        btnEditPersonalInfo.setOnClickListener(this);
        btnAddEditTravelItinerary.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
        btnInviteFriend.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        lblTitle.setText(getString(R.string.settings));
        settingsFragmentPresenter = new SettingsFragmentPresenterImpl(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_edit_personal_info:
                openEditPersonalInfo();
                break;
            case R.id.button_add_edit_travel_itinerary:
                openEditAddTravelItinerary();
                break;
            case R.id.button_message:
                openMessageHistory();
                break;
            case R.id.button_invite_friends:
                openInviteFriends();
                break;
            case R.id.button_logout:
                settingsFragmentPresenter.logout();
                break;
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void openEditPersonalInfo() {
        TransactionManager.getInstance().addFragment(getFragmentManager(),EditProfileFragment.newInstance(UserManager.getInstance().getCurrentUser()));
    }

    @Override
    public void openEditAddTravelItinerary() {

    }

    @Override
    public void openInviteFriends() {

    }

    @Override
    public void openMessageHistory() {

    }

    @Override
    public void gotoLoginScreen() {
        TransactionManager.getInstance().gotoActivity(getActivity(), LoginActivity.class, null, true);
    }

    @Override
    public void showLoading() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(getActivity())
                    .content(getString(R.string.loading_three_dot))
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }

        if(dialog.isShowing()){
           return;
        }

        dialog.show();

    }

    @Override
    public void hideLoading() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
