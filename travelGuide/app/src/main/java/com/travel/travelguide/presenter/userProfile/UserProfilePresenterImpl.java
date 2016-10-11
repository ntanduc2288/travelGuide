package com.travel.travelguide.presenter.userProfile;

import com.travel.travelguide.Bus.object.RatingChangedBusObject;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.services.backendless.BackendlessController;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 10/11/16
 */
public class UserProfilePresenterImpl implements UserProfilePresenter.Presenter{
    private UserProfilePresenter.View view;
    private BackendlessController backendlessController;

    public UserProfilePresenterImpl(UserProfilePresenter.View view, BackendlessController backendlessController) {
        this.view = view;
        this.backendlessController = backendlessController;
    }

    @Override
    public void getAverageRatingNumber(String userId) {
        view.showLoading();
        backendlessController.getRatingEntityObject(userId)
                .flatMap(ratings -> backendlessController.calculateAverageRatingNumber(ratings))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aFloat -> {
                    if(view != null){
                        view.hideLoading();
                        view.bindRatingNumber(aFloat);
                    }
                }, throwable -> {
                    if(view != null){
                        view.hideLoading();
                        view.showMessage(throwable.getMessage());
                    }
                });
    }

    @Override
    public void receivedRatingNumber(User currentUser, RatingChangedBusObject ratingChangedBusObject) {
        if(currentUser.getbackendlessUserId().endsWith(ratingChangedBusObject.getRatingObject().getToUserId())){
            float averageRatingNumber = ratingChangedBusObject.getAverageNumber();
            view.bindRatingNumber(averageRatingNumber);
        }
    }

    @Override
    public void destroy() {
        view = null;
        backendlessController = null;
    }
}
