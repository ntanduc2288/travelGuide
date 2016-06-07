package com.travel.travelguide.presenter.main;

import com.travel.travelguide.Ulti.GeneralCallback;
import com.travel.travelguide.manager.UserManager;

/**
 * Created by user on 6/7/16.
 */
public class MainPresenterImpl extends MainPresenter.Presenter {
    MainPresenter.MainView mainView;

    public MainPresenterImpl(MainPresenter.MainView mainView) {
        this.mainView = mainView;
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
}
