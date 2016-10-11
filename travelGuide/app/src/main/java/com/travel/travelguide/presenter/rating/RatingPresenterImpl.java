package com.travel.travelguide.presenter.rating;

import android.content.Context;

import com.travel.travelguide.Bus.EvenBusHelper;
import com.travel.travelguide.Bus.object.RatingChangedBusObject;
import com.travel.travelguide.Object.entity.Rating;
import com.travel.travelguide.services.backendless.BackendlessController;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 10/11/16
 */
public class RatingPresenterImpl implements RatingPresenter.Presenter {

    private Context context;
    private RatingPresenter.View view;
    private PublishSubject<Float> publishSubjectSubmitButton = PublishSubject.create();
    private Observable<Boolean> observableSubmitButton;
    private BackendlessController backendlessController;

    public RatingPresenterImpl(Context context, RatingPresenter.View view, BackendlessController backendlessController) {
        this.context = context;
        this.view = view;
        this.backendlessController = backendlessController;
        initObservableSubmitButton();
    }

    @Override
    public void initObservableSubmitButton() {

        observableSubmitButton = publishSubjectSubmitButton.map(aFloat -> aFloat > 0 ? true : false);

        observableSubmitButton.subscribe(aBoolean -> {
            if(aBoolean){
                view.enableSubmitButton();
            }else {
                view.disableSubmitButton();
            }
        });
    }

    @Override
    public void sendRatingChangeSignal(float ratingNumber) {
        publishSubjectSubmitButton.onNext(ratingNumber);
    }

    @Override
    public void sendSubmitRatingSignal(String fromUserId, String toUserId, float ratingNumber) {
        view.showLoading();
        backendlessController.submitRating(fromUserId, toUserId, ratingNumber)
                .flatMap(ratingEntityObject -> backendlessController.getRatingEntityObject(ratingEntityObject.getToUserId()))
                .flatMap(ratingEntityObjects -> backendlessController.calculateAverageRatingNumber(ratingEntityObjects))
                .doOnNext(averageNumber -> nofifyRatingChanged(fromUserId, toUserId, ratingNumber, averageNumber))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(averageNumber -> {
                    if(view != null){
                        view.hideLoading();
                        view.hideSubmitMode();
                        view.showThankMode();
                    }
                }, throwable -> {
                    if(view != null){
                        view.hideLoading();
                        view.showMessage(throwable.getMessage());
                    }
                });
    }


    @Override
    public void nofifyRatingChanged(String fromUserId, String toUserId, float ratingNumber, float averageNumber) {
        try {
            Rating ratingEntityObject = new Rating(fromUserId, toUserId, ratingNumber);
            EvenBusHelper.getInstance().notifyRatingChanged(new RatingChangedBusObject(ratingEntityObject, averageNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void destroy() {
        view = null;
    }
}
