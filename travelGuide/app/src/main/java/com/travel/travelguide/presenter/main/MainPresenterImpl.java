package com.travel.travelguide.presenter.main;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;

import com.applozic.mobicomkit.api.conversation.ApplozicMqttIntentService;
import com.applozic.mobicomkit.broadcast.BroadcastService;
import com.applozic.mobicomkit.uiwidgets.conversation.MobiComKitBroadcastReceiver;
import com.travel.travelguide.Ulti.GeneralCallback;
import com.travel.travelguide.manager.UserManager;

/**
 * Created by user on 6/7/16.
 */
public class MainPresenterImpl extends MainPresenter.Presenter {
    private final MobiComKitBroadcastReceiver mobiComKitBroadcastReceiver;
    MainPresenter.MainView mainView;

    public MainPresenterImpl(MainPresenter.MainView mainView) {
        this.mainView = mainView;
        mobiComKitBroadcastReceiver = new MobiComKitBroadcastReceiver((FragmentActivity) mainView.getContext());
    }

    @Override
    public void logout() {
        mainView.showLoading();

        UserManager.getInstance().logoutBKUser(mainView.getContext(), new GeneralCallback(mainView.getContext()) {
            @Override
            public void success(Object o) {
                if (viewIsValid(mainView)) {
                    mainView.hideLoading();
                    mainView.gotoLoginScreen();
                }
            }

            @Override
            public void error(String errorMessage) {
                super.error(errorMessage);
                if (viewIsValid(mainView)) {
                    mainView.hideLoading();
                }
            }
        });
    }

    @Override
    public void destroy() {
        mainView = null;
    }

    @Override
    public void subscribeChat() {
        LocalBroadcastManager.getInstance(mainView.getContext()).registerReceiver(mobiComKitBroadcastReceiver, BroadcastService.getIntentFilter());
        Intent subscribeIntent = new Intent(mainView.getContext(), ApplozicMqttIntentService.class);
        subscribeIntent.putExtra(ApplozicMqttIntentService.SUBSCRIBE, true);
        (mainView.getContext()).startService(subscribeIntent);
    }

    @Override
    public void unSubscribeChat() {

    }


}
