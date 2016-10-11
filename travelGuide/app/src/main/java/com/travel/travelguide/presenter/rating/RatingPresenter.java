package com.travel.travelguide.presenter.rating;

import com.travel.travelguide.Object.User;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 10/11/16
 */
public interface RatingPresenter {
    interface View{
        void bindUserInfo(User user);
        void clickedOnAddCommentButton();
        void clickedOnSubmitButton();
    }

    interface Presenter{

    }
}
