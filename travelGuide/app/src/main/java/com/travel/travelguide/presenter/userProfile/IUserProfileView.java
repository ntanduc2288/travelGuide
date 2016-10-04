package com.travel.travelguide.presenter.userProfile;

import android.content.Context;

import com.travel.travelguide.Object.User;

/**
 * Created by user on 4/29/16.
 */
public interface IUserProfileView {
    void showLoading();
    void hideLoading();
    void bindData(User user);
    void showMessage(String message);
    void updateCurrentTab(int currentTab);
    void gotoConversationActivity();
    Context getContext();
}
