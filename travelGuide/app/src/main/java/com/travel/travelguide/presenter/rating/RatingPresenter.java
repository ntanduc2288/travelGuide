package com.travel.travelguide.presenter.rating;

import com.travel.travelguide.Object.User;

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
        void clickedOnCloseButton();
        void dismissDialog();
        void enableSubmitButton();
        void disableSubmitButton();
        void showSubmitMode();
        void hideSubmitMode();
        void showThankMode();
        void hideThankMode();
    }

    interface Presenter{
        void sendRatingChangeSignal(float ratingNumber);
        void sendSubmitRatingSignal(String fromUserId, String toUserId, float ratingNumber);
        void nofifyRatingChanged(String fromUserId, String toUserId, float ratingNumber, float averageNumber);
        void initObservableSubmitButton();
        void destroy();
    }
}
