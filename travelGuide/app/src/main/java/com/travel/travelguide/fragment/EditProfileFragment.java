package com.travel.travelguide.fragment;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.backendless.geo.GeoPoint;
import com.dd.processbutton.iml.ActionProcessButton;
import com.michael.easydialog.EasyDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.Ulti.CropImageUlti;
import com.travel.travelguide.Ulti.Ulti;
import com.travel.travelguide.View.MultiSelectionSpinner;
import com.travel.travelguide.View.SocialPickerView;
import com.travel.travelguide.activity.LoginActivity;
import com.travel.travelguide.manager.TransactionManager;
import com.travel.travelguide.manager.UserManager;
import com.travel.travelguide.presenter.editProfile.IEditProfileView;
import com.travel.travelguide.presenter.editProfile.ProfilePresenter;
import com.travel.travelguide.presenter.editProfile.ProfilePresenterImpl;
import com.yalantis.ucrop.UCrop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 4/29/16.
 */
public class EditProfileFragment extends BaseFragment implements IEditProfileView, View.OnClickListener, AddPhotoSelectionDialogFrament.IAddPhotoSelelectionListener, MultiSelectionSpinner.OnMultipleItemsSelectedListener  {

    @Bind(R.id.title)
    AppCompatTextView lblTitle;
    @Bind(R.id.back_button)
    AppCompatButton btnBack;
    @Bind(R.id.avatar)
    CircleImageView imgAvatar;
    @Bind(R.id.email)
    AppCompatEditText txtEmail;
    @Bind(R.id.password)
    AppCompatEditText txtPassword;
    @Bind(R.id.name)
    AppCompatEditText txtName;
    @Bind(R.id.confirm_password)
    AppCompatEditText txtConfirmPassword;
    @Bind(R.id.location)
    AppCompatButton btnLocation;
    @Bind(R.id.edit_button)
    AppCompatButton btnEdit;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton btnChat;
    @Bind(R.id.cover_picture)
    AppCompatImageView imgCoverPicture;
    @Bind(R.id.phone)
    AppCompatEditText txtPhone;
    @Bind(R.id.phone_container)
    LinearLayout lnPhoneContainer;
    @Bind(R.id.language)
    MultiSelectionSpinner spnLanguage;
    @Bind(R.id.textview_from)
    AppCompatTextView lblTravelDateFrom;
    @Bind(R.id.textview_to)
    AppCompatTextView lblTravelDateTo;
    @Bind(R.id.relativelayout_travel_date)
    RelativeLayout rlTravelDateContainer;
    @Bind(R.id.linearlayout_travel_from)
    LinearLayout lnTravelDateFrom;
    @Bind(R.id.linearlayout_travel_to)
    LinearLayout lnTravelDateTo;
    @Bind(R.id.button_add_travel_date)
    AppCompatButton btnAddTravelDate;
    @Bind(R.id.lnSocialContainer)
    LinearLayout lnSocialContainer;
    @Bind(R.id.button_add_social) AppCompatButton btnAddSocialLink;
    @Bind(R.id.aboutme) AppCompatEditText txtAboutMe;
    @Bind(R.id.interest) AppCompatEditText txtInterest;
    @Bind(R.id.btnLogout)
    ActionProcessButton btnLogout;
    @Bind(R.id.password_container)
    LinearLayout lnPasswordContainer;
    @Bind(R.id.confirm_password_container)
    LinearLayout lnConfirmPassworContainer;

    private SocialPickerView socialPickerView;
    private EasyDialog easyDialog;

    MaterialDialog dialog;

    String imageLocalPath = Constants.EMPTY_STRING;
    User user;
    ProfilePresenter profilePresenter;
    private Place place;
    private ArrayAdapter<String> languageAdapter;


    public static EditProfileFragment newInstance(User user) {
        EditProfileFragment profileFragment = new EditProfileFragment();
        profileFragment.setUser(user);
        return profileFragment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.edit_profile_screen;
    }

    @Override
    protected void setupViews() {
        toolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black_transparent));
        profilePresenter = new ProfilePresenterImpl(getFragmentManager(), this, this, user);

        btnBack.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
        lnTravelDateFrom.setOnClickListener(this);
        lnTravelDateTo.setOnClickListener(this);
        btnAddTravelDate.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        btnAddSocialLink.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnLogout.setVisibility(View.GONE); //Do not need logout in this screen (keep it in this screen just in case :D)
        String[] languages = Ulti.parseLanguage(getActivity());
//        languageAdapter = new ArrayAdapter<String>(getActivity(), R.layout.language_item, languages);
//        spnLanguage.setAdapter(languageAdapter);
        spnLanguage.setItems(languages);
        spnLanguage.setListener(this);

        profilePresenter.getUserProfile();

    }

    @Override
    public void showLoading() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(getActivity())
                    .content(getString(R.string.loading_three_dot))
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }

        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void bindData(User user) {
        lblTitle.setText(user.getName());
        txtName.setText(user.getName());
        ImageLoader.getInstance().displayImage(user.getAvatar(), imgAvatar);
        txtEmail.setText(user.getEmail());
        btnLocation.setText(user.getLocationName());
        txtPhone.setText(user.getPhoneNumber());
        if(!TextUtils.isEmpty(user.getLanguage())){
            String[] languages = user.getLanguage().split(",");
            spnLanguage.setSelection(languages);
        }
        txtAboutMe.setText(user.getAboutMe());
        txtInterest.setText(user.getInterest());

        if(UserManager.getInstance().haveTravelDate(user)){
            lnTravelDateFrom.setVisibility(View.VISIBLE);
            lnTravelDateTo.setVisibility(View.VISIBLE);

        }else {
            lnTravelDateFrom.setVisibility(View.GONE);
            lnTravelDateTo.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(user.getFacebookLink())){
            profilePresenter.addMoreSocialView(new SocialObject(SocialObject.FACEBOOK_TYPE, user.getFacebookLink()));
        }

        if(!TextUtils.isEmpty(user.getInstagramLink())){
            profilePresenter.addMoreSocialView(new SocialObject(SocialObject.INSTAGRAM_TYPE, user.getInstagramLink()));
        }

        if(!TextUtils.isEmpty(user.getTwitterLink())){
            profilePresenter.addMoreSocialView(new SocialObject(SocialObject.TWITTER_TYPE, user.getTwitterLink()));
        }

    }

    @Override
    public void showMyProfileViews() {
        lnPasswordContainer.setVisibility(View.GONE);
        lnConfirmPassworContainer.setVisibility(View.GONE);
        btnEdit.setVisibility(View.VISIBLE);
        btnChat.setVisibility(View.GONE);
        rlTravelDateContainer.setVisibility(View.GONE);
        if(UserManager.getInstance().haveTravelDate(UserManager.getInstance().getCurrentUser())){
            btnAddTravelDate.setText(R.string.remove_travel_date);
        }else {
            btnAddTravelDate.setText(R.string.add_travel_date);
        }

    }

    @Override
    public void showUserProfileViews() {
        lnPasswordContainer.setVisibility(View.GONE);
        lnConfirmPassworContainer.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        btnChat.setVisibility(View.VISIBLE);
        rlTravelDateContainer.setVisibility(View.GONE);
    }

    @Override
    public void switchToEditMode() {
        txtConfirmPassword.setEnabled(true);
        txtPassword.setEnabled(true);
        txtEmail.setEnabled(false);
        txtName.setEnabled(true);
        btnLocation.setEnabled(true);
        imgAvatar.setEnabled(true);
        imgAvatar.setBorderColor(getResources().getColor(R.color.colorPrimary));
        btnEdit.setBackgroundResource(R.drawable.save_icon);
        txtPhone.setEnabled(true);
        txtPhone.setVisibility(View.VISIBLE);
        lnPhoneContainer.setVisibility(View.VISIBLE);
        txtAboutMe.setEnabled(true);
        txtAboutMe.setVisibility(View.VISIBLE);
        spnLanguage.setEnabled(true);
        btnAddTravelDate.setEnabled(true);
    }

    @Override
    public void switchToViewerMode() {
        txtConfirmPassword.setEnabled(false);
        txtPassword.setEnabled(false);
        txtEmail.setEnabled(false);
        txtName.setEnabled(false);
        txtName.clearFocus();
        btnLocation.setEnabled(false);
        imgAvatar.setEnabled(false);
        imgAvatar.setBorderColor(getResources().getColor(R.color.color_white));
        btnEdit.setBackgroundResource(R.drawable.edit_icon);
        txtPhone.setEnabled(false);
        txtAboutMe.setEnabled(false);
        txtPhone.setVisibility(View.VISIBLE);
        lnPhoneContainer.setVisibility(View.VISIBLE);
        spnLanguage.setEnabled(false);
        btnAddTravelDate.setEnabled(false);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
            case R.id.edit_button:
//                profilePresenter.switchMode();
                editButtonCLicked();
                break;
            case R.id.avatar:
                showAvatarOption();
                break;
            case R.id.location:
                showPlacePicker();
                break;
            case R.id.button_add_travel_date:

                break;
            case R.id.fab:
                Toast.makeText(getActivity(), "Chat feature. Comming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_add_social:
                btnSocialLinkClicked();
                break;
            case R.id.btnLogout:
//                profilePresenter.logout();
                break;
        }
    }

    private void btnSocialLinkClicked() {
        if(profilePresenter.getListSocialsRemainingItems().size() > 0){
            initSocialPicker();
            int[] attachedViewLocation = new int[2];
            btnAddSocialLink.getLocationInWindow(attachedViewLocation);
            easyDialog = new EasyDialog(getActivity()).setLayout(socialPickerView)
                    .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent))
                    .setGravity(EasyDialog.GRAVITY_TOP)
                    .setLocationByAttachedView(btnAddSocialLink)
                    .setTouchOutsideDismiss(true)
                    .setMatchParent(false);
            easyDialog.show();
        }

    }

    private void initSocialPicker() {
        socialPickerView = new SocialPickerView(getActivity(), profilePresenter.getListSocialsRemainingItems(), new SocialPickerView.SelectedSocialCallback() {
            @Override
            public void itemSelected(SocialObject socialObject) {
                profilePresenter.getSocialInfo(socialObject);
                if(easyDialog != null){
                    easyDialog.dismiss();
                }
            }
        });
    }

    private void editButtonCLicked() {
        user.setName(txtName.getText().toString());
        user.setPhoneNumber(txtPhone.getText().toString());
        user.setLanguage(spnLanguage.getSelectedItemsAsString());
        user.setAboutMe(txtAboutMe.getText().toString());
        user.setInterest(txtInterest.getText().toString().trim());
        if(place != null){
            GeoPoint geoPoint = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
            user.setlocation(geoPoint);
            user.setLocationName(place.getName() + " " + place.getAddress());
        }
        for(SocialObject socialObject : profilePresenter.getListSocialsSelectedItems(lnSocialContainer)){
            String socialLink = socialObject.getName();
            switch (socialObject.getId()){
                case SocialObject.FACEBOOK_TYPE:
                    user.setFacebookLink(socialLink);
                    break;
                case SocialObject.INSTAGRAM_TYPE:
                    user.setInstagramLink(socialLink);
                    break;
                case SocialObject.TWITTER_TYPE:
                    user.setTwitterLink(socialLink);
                    break;
            }
        }
        profilePresenter.updateUserProfile(imageLocalPath);

    }

    private void showPlacePicker() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(getActivity()), Constants.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    //////////////////////
    AddPhotoSelectionDialogFrament addPhotoSelectionDialogFrament;

    private void showAvatarOption() {

        if (addPhotoSelectionDialogFrament == null) {
            addPhotoSelectionDialogFrament = AddPhotoSelectionDialogFrament.newInstance();
            addPhotoSelectionDialogFrament.setOnAddPhotoSelectionListener(this);
        }
        addPhotoSelectionDialogFrament.show(getFragmentManager(), "Add photo");

    }

    @Override
    public void onPressChooosePhoto() {
        pickFromGallery();
    }

    @Override
    public void onPressTakePhoto() {
        imageLocalPath = profilePresenter.getImageLocalPath();
        CropImageUlti.startCamera(this, imageLocalPath, Constants.CAMERA_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.REQUEST_SELECT_PICTURE:
                if(data != null){

                    final Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        imageLocalPath = profilePresenter.getImageLocalPath();
                        CropImageUlti.startCropActivity(this, data.getData(), imageLocalPath);
                    } else {
                        Toast.makeText(getActivity(), R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case UCrop.REQUEST_CROP:
                if(data != null){
                    handleCropResultFromUCrop(UCrop.getOutput(data));
                }
                break;
            case UCrop.RESULT_ERROR:
                if(data!= null){
                    handleCropError(data);
                }
                break;
            case Constants.CAMERA_CODE:

                handleImageResultFromTakePicture();
                break;
            case Constants.PLACE_PICKER_REQUEST:
                if(data != null){
                    place = PlacePicker.getPlace(getActivity(), data);

                    String toastMsg = place.getName() + " " + place.getAddress();
                    displayLocation(toastMsg);
                }
                break;
        }


    }

    private void displayLocation(String location) {
        btnLocation.setText(location);
    }

    private void handleImageResultFromTakePicture() {
        File file = new File(imageLocalPath);
        boolean exists = file.exists();
        if (exists) {
            CropImageUlti.startCropActivity(this, Uri.fromFile(file), imageLocalPath);
        } else
            Toast.makeText(getActivity().getApplicationContext(),
                    "Something goes wrong while taking picture, please try again.",
                    Toast.LENGTH_SHORT).show();
    }

    private void handleCropResultFromUCrop(Uri imgUri) {

        if (imgUri != null) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            Bitmap bitmap = CropImageUlti.getPic(width, height, imgUri.getPath());
            imgAvatar.setImageBitmap(bitmap);

        } else {
            Toast.makeText(getActivity(), R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            CropImageUlti.pickFromGallery(this, Constants.REQUEST_SELECT_PICTURE);
        }
    }


    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(EditProfileFragment.class.getSimpleName(), "handleCropError: ", cropError);
            Toast.makeText(getActivity(), cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialog != null) {
            dialog.dismiss();
        }
        socialPickerView = null;
        profilePresenter.releaseResources();
    }

    @Override
    public void showAddSocialButton() {
        btnAddSocialLink.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddSocialButton() {
        btnAddSocialLink.setVisibility(View.GONE);
    }

    @Override
    public void updateUserInfoSuccessfull(User user) {
        imageLocalPath = Constants.EMPTY_STRING;

    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }

    @Override
    public void gotoLoginScreen() {
        TransactionManager.getInstance().gotoActivity(getActivity(), LoginActivity.class, null, true);
    }

    @Override
    public LinearLayout getLayoutSocialContainer() {
        return lnSocialContainer;
    }
}
