package com.travel.travelguide.presenter.rating;

import com.travel.travelguide.Object.RatingEntityObject;
import com.travel.travelguide.Object.User;

import rx.Observable;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 10/11/16
 */
public interface RatingPresenter {
    interface View{
        void showLoading();
        void hideLoading();
        void showMessage(String message);
        void bindUserInfo(User user);
        void clickedOnAddCommentButton();
        void clickedOnSubmitButton();
        void dismissDialog();
        void enableSubmitButton();
        void disableSubmitButton();
    }

    interface Presenter{
        void sendRatingChangeSignal(float ratingNumber);
        void sendSubmitRatingSignal(String fromUserId, String toUserId, float ratingNumber);
        Observable<RatingEntityObject> submitRating(String fromUserId, String toUserId, float ratingNumber);
        void nofifyRatingChanged(RatingEntityObject ratingObject);
        void initObservableSubmitButton();
        void destroy();
    }
}
