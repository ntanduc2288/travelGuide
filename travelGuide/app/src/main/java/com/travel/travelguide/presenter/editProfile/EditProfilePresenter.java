package com.travel.travelguide.presenter.editProfile;

import android.content.Context;
import android.widget.LinearLayout;

import com.github.gorbin.asne.core.persons.SocialPerson;
import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.presenter.IBasePresenter;

import java.util.ArrayList;

/**
 * Created by user on 4/29/16.
 */
public interface EditProfilePresenter {
    interface View{
        void showLoading();
        void hideLoading();
        void bindData(User user);
        void showMyProfileViews();
        void showUserProfileViews();
        void switchToEditMode();
        void switchToViewerMode();
        void showMessage(String message);
        Context getContext();
        void showAddSocialIconView();
        void hideAddSocialIconView();
        void updateUserInfoSuccessfull(User user);
        void gotoLoginScreen();
        void initSocialPicker();
        LinearLayout getLayoutSocialContainer();
        void bindRatingNumber(float ratingNumber);
    }

    interface Presenter extends IBasePresenter {
        void getUserProfile();
        void updateUserProfile();
        void updateUserProfile(String avatarFile);
        void setImageLocalPath(String imagePath);
        String getImageLocalPath();
        void switchMode();
        void error(Integer errorCode);
        ArrayList<SocialObject> getListSocialsRemainingItems();
        ArrayList<SocialObject> getListSocialsSelectedItems(LinearLayout lnContainer);
        void addMoreSocialView(int socialNetworkID, SocialPerson socialPerson);
        void addMoreSocialView(SocialObject socialObject);
        void getSocialInfo(SocialObject socialObject);
        void getAverageRatingNumber(String userId);
    }

}
