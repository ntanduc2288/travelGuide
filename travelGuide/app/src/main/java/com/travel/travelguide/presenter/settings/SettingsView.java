package com.travel.travelguide.presenter.settings;

import android.content.Context;

/**
 * Created by user on 5/27/16.
 */
public interface SettingsView {
    void showLoading();
    void hideLoading();
    void openEditPersonalInfo();
    void openEditAddTravelItinerary();
    void openInviteFriends();
    void openMessageHistory();
    void gotoLoginScreen();
    void showMessage(String message);
    Context getContext();
}
