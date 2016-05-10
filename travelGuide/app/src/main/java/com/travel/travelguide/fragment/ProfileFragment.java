package com.travel.travelguide.fragment;

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
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.backendless.geo.GeoPoint;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.Ulti.CropImageUlti;
import com.travel.travelguide.Ulti.Ulti;
import com.travel.travelguide.manager.UserManager;
import com.travel.travelguide.presenter.profile.IProfileView;
import com.travel.travelguide.presenter.profile.ProfilePresenter;
import com.travel.travelguide.presenter.profile.ProfilePresenterImpl;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.Bind;

/**
 * Created by user on 4/29/16.
 */
public class ProfileFragment extends BaseFragment implements IProfileView, View.OnClickListener, AddPhotoSelectionDialogFrament.IAddPhotoSelelectionListener {

    @Bind(R.id.title)
    AppCompatTextView lblTitle;
    @Bind(R.id.back_button)
    AppCompatButton btnBack;
    @Bind(R.id.avatar)
    CircularImageView imgAvatar;
    @Bind(R.id.email)
    AppCompatEditText txtEmail;
    @Bind(R.id.password)
    AppCompatEditText txtPassword;
    @Bind(R.id.name)
    AppCompatEditText txtName;
    @Bind(R.id.facebook)
    AppCompatEditText txtFacebook;
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
    @Bind(R.id.language)
    AppCompatSpinner spnLanguage;
    @Bind(R.id.twitter)
    AppCompatEditText txtTwitter;
    @Bind(R.id.instagram)
    AppCompatEditText txtInstagram;
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


    MaterialDialog dialog;

    String imageLocalPath = Constants.EMPTY_STRING;
    User user;
    ProfilePresenter profilePresenter;
    private Place place;
    private ArrayAdapter<String> languageAdapter;


    public static ProfileFragment newInstance(User user) {
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setUser(user);
        return profileFragment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.profile_screen;
    }

    @Override
    protected void setupViews() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.black_transparent));
        profilePresenter = new ProfilePresenterImpl(this, user);

        btnBack.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
        lnTravelDateFrom.setOnClickListener(this);
        lnTravelDateTo.setOnClickListener(this);
        btnAddTravelDate.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        String[] languages = Ulti.parseLanguage(getActivity());
        languageAdapter = new ArrayAdapter<String>(getActivity(), R.layout.language_item, languages);
        spnLanguage.setAdapter(languageAdapter);

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
        txtFacebook.setText(user.getFacebookLink());
        btnLocation.setText(user.getLocationName());
        txtInstagram.setText(user.getInstagramLink());
        txtTwitter.setText(user.getTwitterLink());
        txtPhone.setText(user.getPhoneNumber());
        spnLanguage.setSelection(languageAdapter.getPosition(user.getLanguage()));

        if(UserManager.getInstance().haveTravelDate(user)){
            lnTravelDateFrom.setVisibility(View.VISIBLE);
            lnTravelDateTo.setVisibility(View.VISIBLE);

        }else {
            lnTravelDateFrom.setVisibility(View.GONE);
            lnTravelDateTo.setVisibility(View.GONE);
        }

    }

    @Override
    public void showMyProfileViews() {
        txtPassword.setVisibility(View.GONE);
        txtConfirmPassword.setVisibility(View.GONE);
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
        txtPassword.setVisibility(View.GONE);
        txtConfirmPassword.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        btnChat.setVisibility(View.VISIBLE);
        rlTravelDateContainer.setVisibility(View.GONE);
    }

    @Override
    public void switchToEditMode() {
        txtConfirmPassword.setEnabled(true);
        txtPassword.setEnabled(true);
        txtEmail.setEnabled(false);
        txtFacebook.setEnabled(true);
        txtName.setEnabled(true);
        btnLocation.setEnabled(true);
        imgAvatar.setEnabled(true);
        imgAvatar.setBorderColor(getResources().getColor(R.color.colorPrimary));
        btnEdit.setBackgroundResource(R.drawable.save_icon);
        txtPhone.setEnabled(true);
        txtInstagram.setEnabled(true);
        txtTwitter.setEnabled(true);
        spnLanguage.setEnabled(true);
        btnAddTravelDate.setEnabled(true);
    }

    @Override
    public void switchToViewerMode() {
        txtConfirmPassword.setEnabled(false);
        txtPassword.setEnabled(false);
        txtEmail.setEnabled(false);
        txtFacebook.setEnabled(false);
        txtName.setEnabled(false);
        txtName.clearFocus();
        btnLocation.setEnabled(false);
        imgAvatar.setEnabled(false);
        imgAvatar.setBorderColor(getResources().getColor(R.color.color_white));
        btnEdit.setBackgroundResource(R.drawable.edit_icon);
        txtPhone.setEnabled(false);
        txtInstagram.setEnabled(false);
        txtTwitter.setEnabled(false);
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
        }
    }

    private void editButtonCLicked() {
        user.setName(txtName.getText().toString());
        user.setPhoneNumber(txtPhone.getText().toString());
        user.setLanguage((String) spnLanguage.getSelectedItem());
        user.setFacebookLink(txtFacebook.getText().toString());
        user.setTwitterLink(txtTwitter.getText().toString());
        user.setInstagramLink(txtInstagram.getText().toString());
        if(place != null){
            GeoPoint geoPoint = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
            user.setlocation(geoPoint);
            user.setLocationName(place.getName() + " " + place.getAddress());
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
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    imageLocalPath = profilePresenter.getImageLocalPath();
                    CropImageUlti.startCropActivity(this, data.getData(), imageLocalPath);
                } else {
                    Toast.makeText(getActivity(), R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
                break;
            case UCrop.REQUEST_CROP:
                handleCropResultFromUCrop(UCrop.getOutput(data));
                break;
            case UCrop.RESULT_ERROR:
                handleCropError(data);
                break;
            case Constants.CAMERA_CODE:

                handleImageResultFromTakePicture();
                break;
            case Constants.PLACE_PICKER_REQUEST:
                place = PlacePicker.getPlace(getActivity(), data);

                String toastMsg = place.getName() + " " + place.getAddress();
                displayLocation(toastMsg);
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
            Log.e(ProfileFragment.class.getSimpleName(), "handleCropError: ", cropError);
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
        profilePresenter.releaseResources();
    }
}
