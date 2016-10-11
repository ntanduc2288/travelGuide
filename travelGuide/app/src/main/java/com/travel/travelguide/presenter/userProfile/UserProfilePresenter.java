package com.travel.travelguide.presenter.userProfile;

import android.content.Context;

import com.travel.travelguide.Bus.object.RatingChangedBusObject;
import com.travel.travelguide.Object.User;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 10/11/16
 */
public interface UserProfilePresenter {
    interface View{
        void showLoading();
        void hideLoading();
        void bindData(User user);
        void showMessage(String message);
        void updateCurrentTab(int currentTab);
        void gotoConversationActivity();
        void openRatingDialog();
        void bindRatingNumber(float ratingNumber);
        Context getContext();
        void receivedRatingChangedSignal(RatingChangedBusObject ratingChangedBusObject);
    }

    interface Presenter{
        void getAverageRatingNumber(String userId);
        void receivedRatingNumber(User currentUser, RatingChangedBusObject ratingChangedBusObject);
        void destroy();
    }
}
