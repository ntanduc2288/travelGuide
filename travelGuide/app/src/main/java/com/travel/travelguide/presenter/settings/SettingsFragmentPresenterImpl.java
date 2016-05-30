package com.travel.travelguide.presenter.settings;

import com.travel.travelguide.Ulti.GeneralCallback;
import com.travel.travelguide.manager.UserManager;

/**
 * Created by user on 5/27/16.
 */
public class SettingsFragmentPresenterImpl extends SettingsFragmentPresenter {
    SettingsView settingsView;
    public SettingsFragmentPresenterImpl(SettingsView settingsView){
        this.settingsView = settingsView;
    }

    @Override
    public void logout() {
        settingsView.showLoading();

        UserManager.getInstance().logoutBKUser(settingsView.getContext(), new GeneralCallback(settingsView.getContext()) {
            @Override
            public void success(Object o) {
                if (viewIsValid(settingsView)) {
                    settingsView.hideLoading();
                    settingsView.gotoLoginScreen();
                }
            }

            @Override
            public void error(String errorMessage) {
                super.error(errorMessage);
                if (viewIsValid(settingsView)) {
                    settingsView.hideLoading();
                }
            }
        });

    }

    @Override
    public void destroy() {

    }


}
