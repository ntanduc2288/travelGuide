package com.travel.travelguide.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.Ulti.CropImageUlti;
import com.travel.travelguide.activity.LoginActivity;
import com.travel.travelguide.manager.TransactionManager;
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
    EditText txtEmail;
    @Bind(R.id.password)
    EditText txtPassword;
    @Bind(R.id.name)
    EditText txtName;
    @Bind(R.id.facebook)
    EditText txtFacebook;
    @Bind(R.id.confirm_password)
    EditText txtConfirmPassword;
    @Bind(R.id.location)
    Button btnLocation;
    @Bind(R.id.edit_button)
    Button btnEdit;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton btnChat;
    @Bind(R.id.logout)
    Button btnLogout;
    @Bind(R.id.cover_picture)
    AppCompatImageView imgCoverPicture;

    User user;
    ProfilePresenter profilePresenter;


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
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        profilePresenter = new ProfilePresenterImpl(this, user);
        profilePresenter.getUserProfile();
        btnBack.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void bindData(User user) {
//        lblTitle.setText(user.getName());
        txtEmail.setText(user.getName());
        ImageLoader.getInstance().displayImage(user.getAvatar(), imgAvatar);
        txtEmail.setText(user.getEmail());
        txtFacebook.setText(user.getFacebookLink());
        btnLocation.setText(user.getLocationName());
    }

    @Override
    public void showMyProfileViews() {
        txtPassword.setVisibility(View.VISIBLE);
        txtConfirmPassword.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        btnChat.setVisibility(View.GONE);
    }

    @Override
    public void showUserProfileViews() {
        txtPassword.setVisibility(View.GONE);
        txtConfirmPassword.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        btnChat.setVisibility(View.VISIBLE);
    }

    @Override
    public void switchToEditMode() {
        txtConfirmPassword.setEnabled(true);
        txtPassword.setEnabled(true);
        txtEmail.setEnabled(true);
        txtFacebook.setEnabled(true);
        txtName.setEnabled(true);
        btnLocation.setEnabled(true);
        imgAvatar.setEnabled(true);
        imgAvatar.setBorderColor(getResources().getColor(R.color.colorPrimary));
        btnEdit.setBackgroundResource(R.drawable.save_icon);
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
    }

    @Override
    public void gotoLoginScreen() {
        TransactionManager.getInstance().gotoActivity(getActivity(), LoginActivity.class, null, true);
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
                profilePresenter.switchMode();
                break;
            case R.id.logout:
                profilePresenter.logout();
                break;
            case R.id.avatar:
                showAvatarOption();
                break;
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
        CropImageUlti.startCamera(this, IMAGE_TAKE_PICTURE_NAME, Constants.CAMERA_CODE);
    }

    private static final int REQUEST_SELECT_PICTURE = 0x01;
//    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.png";
    private static final String IMAGE_TAKE_PICTURE_NAME = Environment
            .getExternalStorageDirectory().getPath() + "/travelGuideTmpImage.jpg";


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SELECT_PICTURE:
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    CropImageUlti.startCropActivity(this, data.getData(), IMAGE_TAKE_PICTURE_NAME);
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
        }


    }

    private void handleImageResultFromTakePicture() {
        File file = new File(IMAGE_TAKE_PICTURE_NAME);
        boolean exists = file.exists();
        if (exists) {
            CropImageUlti.startCropActivity(this, Uri.fromFile(file), IMAGE_TAKE_PICTURE_NAME);
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
            CropImageUlti.pickFromGallery(this, REQUEST_SELECT_PICTURE);
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
}
