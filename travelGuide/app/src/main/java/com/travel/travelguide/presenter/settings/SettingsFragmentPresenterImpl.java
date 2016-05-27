package com.travel.travelguide.presenter.settings;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.travel.travelguide.manager.UserManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by user on 5/27/16.
 */
public class SettingsFragmentPresenterImpl extends SettingsFragmentPresenter {
    SettingsView settingsView;
    CompositeSubscription compositeSubscription;
    public SettingsFragmentPresenterImpl(SettingsView settingsView){
        this.settingsView = settingsView;
    }

    @Override
    public void logout() {
        settingsView.showLoading();


        Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                clearLocalUserData();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (viewIsValid(settingsView)) {
                    settingsView.hideLoading();
                    settingsView.showMessage(fault.getMessage());
                }
            }
        });

    }

    private void clearLocalUserData() {

        Observable observable = Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return Observable.just(UserManager.getInstance().clearCurrentUserInfo(settingsView.getContext().getApplicationContext()));
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        if(compositeSubscription == null){
            compositeSubscription = new CompositeSubscription();
        }

        compositeSubscription.add(observable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (viewIsValid(settingsView)) {
                    settingsView.hideLoading();
                    settingsView.gotoLoginScreen();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (viewIsValid(settingsView)) {
                    settingsView.hideLoading();
                    settingsView.showMessage("Could not logout");
                }
            }
        }));
    }

    @Override
    public void destroy() {

    }


}
