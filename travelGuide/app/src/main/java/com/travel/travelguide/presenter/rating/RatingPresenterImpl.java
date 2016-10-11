package com.travel.travelguide.presenter.rating;

import android.content.Context;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.travel.travelguide.Bus.EvenBusHelper;
import com.travel.travelguide.Bus.object.RatingChangedBusObject;
import com.travel.travelguide.Object.RatingEntityObject;

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

    public RatingPresenterImpl(Context context, RatingPresenter.View view) {
        this.context = context;
        this.view = view;
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
        submitRating(fromUserId, toUserId, ratingNumber)
                .doOnNext(ratingEntityObject -> nofifyRatingChanged(ratingEntityObject))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ratingObject -> {
                    if(view != null){
                        view.hideLoading();
                        view.dismissDialog();
                    }
                }, throwable -> {
                    if(view != null){
                        view.hideLoading();
                        view.showMessage(throwable.getMessage());
                    }
                });
    }

    @Override
    public Observable<RatingEntityObject> submitRating(String fromUserId, String toUserId, float ratingNumber) {
        RatingEntityObject ratingObjectTmp = new RatingEntityObject(fromUserId, toUserId, ratingNumber);

        return Observable.create(subscriber -> {
            Backendless.Persistence.of(RatingEntityObject.class).save(ratingObjectTmp, new AsyncCallback<RatingEntityObject>() {
                @Override
                public void handleResponse(RatingEntityObject response) {
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    subscriber.onError(new Throwable(fault.getMessage()));
                }
            });


        });
    }

    @Override
    public void nofifyRatingChanged(RatingEntityObject ratingObject) {
        try {
            EvenBusHelper.getInstance().notifyRatingChanged(new RatingChangedBusObject(ratingObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        view = null;
    }
}
