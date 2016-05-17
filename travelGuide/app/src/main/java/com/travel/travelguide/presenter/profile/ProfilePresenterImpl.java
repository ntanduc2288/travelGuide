package com.travel.travelguide.presenter.profile;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.Ulti.CropImageUlti;
import com.travel.travelguide.Ulti.LogUtils;
import com.travel.travelguide.View.SocialItemView;
import com.travel.travelguide.manager.UserManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by user on 4/29/16.
 */
public class ProfilePresenterImpl implements ProfilePresenter {
    private final String TAG = ProfilePresenterImpl.class.getSimpleName();
    IProfileView profileView;
    User user;
    boolean isMyProfileView;
    boolean isInEditMode;
    private CompositeSubscription compositeSubscription;
    private String imageLocalPath = Constants.EMPTY_STRING;
    ArrayList<SocialObject> socialObjectsOriginal;
    ArrayList<SocialObject> socialObjectsSelected;
    String avatarLocalFile = "";

    public ProfilePresenterImpl(IProfileView profileView, User user) {
        this.profileView = profileView;
        this.user = user;
        isMyProfileView = user.getbackendlessUserId().equalsIgnoreCase(UserManager.getInstance().getCurrentUser().getbackendlessUserId());
        socialObjectsOriginal = new ArrayList<>();
        socialObjectsOriginal.add(new SocialObject(SocialObject.FACEBOOK_TYPE, Constants.EMPTY_STRING));
        socialObjectsOriginal.add(new SocialObject(SocialObject.TWITTER_TYPE, Constants.EMPTY_STRING));
        socialObjectsOriginal.add(new SocialObject(SocialObject.INSTAGRAM_TYPE, Constants.EMPTY_STRING));
        socialObjectsSelected = new ArrayList<>();
    }

    @Override
    public void getUserProfile() {
        profileView.showLoading();
        profileView.bindData(user);
        if (isMyProfileView) {
            profileView.showMyProfileViews();
        } else {
            profileView.showUserProfileViews();
        }
        profileView.switchToEditMode();
        profileView.hideLoading();
    }

    public void setImageLocalPath(String imageLocalPath) {
        this.imageLocalPath = imageLocalPath;
    }

    @Override
    public void switchMode() {
        if (isInEditMode) {
            //Save data and switch to viewer mode
            updateUserProfile(imageLocalPath);

        } else {
            profileView.switchToEditMode();
        }
        isInEditMode = !isInEditMode;
    }

    @Override
    public void updateUserProfile() {
        profileView.showLoading();
    }

    @Override
    public void updateUserProfile(final String avatarFile) {

        profileView.showLoading();
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }

        if (!TextUtils.isEmpty(avatarFile)) {
            rx.Subscription subscription = getFile(avatarFile).subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap bitmap) {
                    if (bitmap != null) {
                        uploadFileToServer(avatarFile);
                    } else {
                        profileView.hideLoading();
                    }
                }
            });
            compositeSubscription.add(subscription);
        } else {
            updateUserProfileToServer(user);
        }


    }

    private void uploadFileToServer(String filePath) {
        Backendless.Files.upload(new File(filePath), Constants.IMAGE_FOLDER_SERVER, new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile response) {

                String imagePath = response.getFileURL();
                user.setAvatar(imagePath);
                updateUserProfileToServer(user);
                LogUtils.logD(TAG, "upload file: " + response.toString());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(viewIsValid()){
                    profileView.hideLoading();
                    profileView.showMessage(fault.getMessage());
                }
                LogUtils.logD(TAG, "upload file error: " + fault.getMessage());
            }
        });
    }

    private void updateUserProfileToServer(final User user) {
        Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                if(viewIsValid()){
                    profileView.hideLoading();
//                    profileView.switchToViewerMode();
                    profileView.showMessage(profileView.getContext().getString(R.string.your_account_has_been_updated));
                    UserManager.getInstance().setCurrentUser(new User(response));
                    profileView.updateUserInfoSuccessfull(user);
                }
                LogUtils.logD(TAG, "update profile: " + response.toString());

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(viewIsValid()){
                    profileView.hideLoading();
                    profileView.showMessage(fault.getMessage());
                }
                LogUtils.logD(TAG, "update profile error: " + fault.getMessage());
            }
        });
    }

    @Override
    public String getImageLocalPath() {
        imageLocalPath =  Environment.getExternalStorageDirectory().getPath() + "/" + user.getbackendlessUserId() + "_" +Calendar.getInstance().getTimeInMillis() + ".jpg";
        return imageLocalPath;
    }

    @Override
    public void error(Integer errorCode) {
        profileView.hideLoading();
        profileView.showMessage("Testing");
    }

    private Observable<Bitmap> getFile(final String filePath) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                return Observable.just(CropImageUlti.getPic(filePath)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
            }
        });

    }


    @Override
    public void releaseResources() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
        profileView = null;
    }

    @Override
    public boolean viewIsValid() {
        if (profileView != null) {
            return true;
        } else {
            return false;
        }
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
            if(viewGroup instanceof SocialItemView){
                socialObjects.add(((SocialItemView)viewGroup).getSocialObject());
            }
        }
        return socialObjects;
    }

    @Override
    public void addMoreSocialView(LinearLayout lnContainer, final SocialObject socialObject) {
        SocialItemView socialItemView = new SocialItemView(profileView.getContext(), socialObject, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socialObjectsSelected.remove(socialObject);
                checkShowHideAddSocialButton();
            }
        });
        lnContainer.addView(socialItemView);
        socialObjectsSelected.add(socialObject);
        checkShowHideAddSocialButton();
    }

    private void checkShowHideAddSocialButton(){
        if(socialObjectsSelected.size() == socialObjectsOriginal.size()){
            profileView.hideAddSocialButton();
        }else {
            profileView.showAddSocialButton();
        }
    }

}
