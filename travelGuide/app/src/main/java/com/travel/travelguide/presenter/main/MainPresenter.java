package com.travel.travelguide.presenter.main;

import android.content.Context;

import com.travel.travelguide.presenter.BasePresenters;

/**
 * Created by user on 6/7/16.
 */
public interface MainPresenter {
    public interface MainView{
        public void showLoading();
        public void hideLoading();
        public void gotoLoginScreen();
        public Context getContext();
    }

    public abstract class Presenter extends BasePresenters{
        public abstract void logout();
        public abstract void destroy();
    }
}
