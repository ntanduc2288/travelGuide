package com.travel.travelguide.presenter.register;

import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.Ulti.GeneralCallback;
import com.travel.travelguide.Ulti.Ulti;
import com.travel.travelguide.View.SocialItemEditText;
import com.travel.travelguide.manager.UserManager;
import com.travel.travelguide.presenter.BaseSocialPresenterImpl;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by user on 4/23/16.
 */
public class RegisterPresenterImpl extends BaseSocialPresenterImpl implements RegisterPresenter {

    IRegisterView registerView;
    private final String TAG = RegisterPresenterImpl.class.getSimpleName();
    ArrayList<SocialObject> socialObjectsOriginal;
    ArrayList<SocialObject> socialObjectsSelected;

    SocialNetworkManager mSocialNetworkManager;

    public RegisterPresenterImpl(FragmentManager fragmentManager, Fragment fragment, IRegisterView registerView) {
        super(fragmentManager, fragment);
        this.registerView = registerView;
        socialObjectsOriginal = new ArrayList<>();
        socialObjectsOriginal.add(new SocialObject(SocialObject.FACEBOOK_TYPE, Constants.EMPTY_STRING));
        socialObjectsOriginal.add(new SocialObject(SocialObject.TWITTER_TYPE, Constants.EMPTY_STRING));
        socialObjectsOriginal.add(new SocialObject(SocialObject.INSTAGRAM_TYPE, Constants.EMPTY_STRING));
        socialObjectsSelected = new ArrayList<>();
    }

    @Override
    public void setSocialNetworkManager(SocialNetworkManager socialNetworkManager) {
        this.mSocialNetworkManager = socialNetworkManager;
    }

    @Override
    public void validateData(User user, String password, String confirmPassword) {

        if(viewIsValid()){
            registerView.showLoading();
            if(!Ulti.isEmailValid(user.getEmail())){
                registerView.hideLoading();
                registerView.invalidEmail();
            }else if (!password.equals(confirmPassword) || password.length() < 3){
                registerView.hideLoading();
                registerView.invalidPassword();
            }else if (TextUtils.isEmpty(user.getLocationName())){
                registerView.hideLoading();
                registerView.invalidLocation();
            }else {
                user.setPassword(password);
                register(user);
            }
        }

    }

    @Override
    public void register(User user) {
        UserManager.getInstance().signUpBKUser(registerView.getContext(), user, new GeneralCallback(registerView.getContext()) {
            @Override
            public void success(Object o) {
                if(viewIsValid()){
                    registerView.hideLoading();
                    registerView.gotoMapScreen();
                }
            }

            @Override
            public void error(String errorMessage) {
                super.error(errorMessage);
                if(viewIsValid()){
                    registerView.hideLoading();
                }
            }
        });
    }

    @Override
    public ArrayList<SocialObject> getListSocialsRemainingItems() {
        ArrayList<SocialObject> objectsRemaining = new ArrayList<>();
        objectsRemaining.addAll(socialObjectsOriginal);
        for(SocialObject socialObjectSelected : socialObjectsSelected){
            for(SocialObject socialObjectOriginal : objectsRemaining){
                if(socialObjectOriginal.getId() == socialObjectSelected.getId()){
                    objectsRemaining.remove(socialObjectOriginal);
                    break;
                }
            }
        }

        return objectsRemaining;
    }

    @Override
    public ArrayList<SocialObject> getListSocialsSelectedItems(LinearLayout lnContainer) {
        ArrayList<SocialObject> socialObjects = new ArrayList<>();
        for (int i = 0; i < lnContainer.getChildCount(); i++) {
            View viewGroup = lnContainer.getChildAt(i);
            if(viewGroup instanceof SocialItemEditText){
                socialObjects.add(((SocialItemEditText)viewGroup).getSocialObject());
            }
        }
        return socialObjects;
    }

    @Override
    public void addMoreSocialView(int socialNetworkID, SocialPerson socialPerson) {
        SocialObject socialObject = new SocialObject(socialNetworkID, Ulti.getSocialLink(socialPerson));
        SocialItemEditText socialItemView = new SocialItemEditText(registerView.getContext(), socialObject, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socialObjectsSelected.remove(socialObject);
                checkShowHideAddSocialButton();
            }
        });
        registerView.getLayoutSocialContainer().addView(socialItemView);
        socialObjectsSelected.add(socialObject);
        checkShowHideAddSocialButton();

    }

    @Override
    public void getSocialInfo(SocialObject socialObject){
        requestDetailInfo(socialObject.getId());
    }

    private void checkShowHideAddSocialButton(){
        if(socialObjectsSelected.size() == socialObjectsOriginal.size()){
            registerView.hideAddSocialButton();
        }else {
            registerView.showAddSocialButton();
        }
    }

    @Override
    public void error(Integer errorCode) {

    }

    @Override
    public boolean viewIsValid(){
        if(registerView != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void releaseResources() {
        registerView = null;
    }


    @Override
    public void onRequestDetailedSocialPersonSuccess(int socialNetworkID, SocialPerson socialPerson) {
        super.onRequestDetailedSocialPersonSuccess(socialNetworkID, socialPerson);
        int id = 0;
        addMoreSocialView(socialNetworkID, socialPerson);
        registerView.hideLoading();
    }


    @Override
    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
        super.onError(socialNetworkID, requestID, errorMessage, data);
        int id = 0;
        registerView.hideLoading();
        registerView.showError(errorMessage);
    }

    @Override
    public void startGetSocialInfo() {
        registerView.showLoading();
    }
}
