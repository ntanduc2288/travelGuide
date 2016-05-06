package com.travel.travelguide.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.geo.GeoPoint;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.activity.MainActivity;
import com.travel.travelguide.manager.TransactionManager;
import com.travel.travelguide.presenter.register.IRegisterView;
import com.travel.travelguide.presenter.register.RegisterPresenter;
import com.travel.travelguide.presenter.register.RegisterPresenterImpl;

import java.util.Calendar;

import butterknife.Bind;

/**
 * Created by user on 4/23/16.
 */
public class RegisterFragment extends BaseFragment implements IRegisterView, View.OnClickListener {
    @Bind(R.id.email)
    AppCompatEditText txtEmail;
    @Bind(R.id.password)
    AppCompatEditText txtPassword;
    @Bind(R.id.btnRegister)
    ActionProcessButton btnActionRegister;
    @Bind(R.id.name) AppCompatEditText txtName;
    @Bind(R.id.facebook) AppCompatEditText txtFacebook;
    @Bind(R.id.confirm_password) AppCompatEditText txtConfirmPassword;
    @Bind(R.id.location)
    AppCompatButton lblLocation;
    @Bind(R.id.title)
    TextView lblTitle;
    @Bind(R.id.back_button)
    AppCompatButton btnBack;
    @Bind(R.id.phone) AppCompatEditText txtPhone;
    @Bind(R.id.language) AppCompatSpinner spnLanguage;
    @Bind(R.id.twitter) AppCompatEditText txtTwitter;
    @Bind(R.id.instagram) AppCompatEditText txtInstagram;

    GoogleApiClient googleApiClient;
    RegisterPresenter registerPresenter;
    private Place place;

    public static RegisterFragment newInstance(){
        return new RegisterFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.register_fragment;
    }

    @Override
    protected void setupViews() {
        registerPresenter = new RegisterPresenterImpl(this);
        btnActionRegister.setOnClickListener(this);
        btnActionRegister.setMode(ActionProcessButton.Mode.ENDLESS);
        lblLocation.setOnClickListener(this);
        lblTitle.setText(getString(R.string.create_account));
        txtEmail.setText("user"+ Calendar.getInstance().getTimeInMillis() + "@gmail.com");
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage((FragmentActivity) getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .build();
        btnBack.setOnClickListener(this);
    }

    @Override
    public void showLoading() {
        btnActionRegister.setProgress(50);
        btnActionRegister.setEnabled(false);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void invalidEmail() {
        txtEmail.setError(getString(R.string.invalid_email));
        btnActionRegister.setProgress(0);
        btnActionRegister.setEnabled(true);
    }

    @Override
    public void invalidPassword() {
        txtPassword.setError(getString(R.string.invalid_password));
        btnActionRegister.setProgress(0);
        btnActionRegister.setEnabled(true);
    }

    @Override
    public void invalidLocation() {
        btnActionRegister.setProgress(0);
        btnActionRegister.setEnabled(true);
        Toast.makeText(getActivity(), getString(R.string.please_set_you_location), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(Integer errorCode) {
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
        btnActionRegister.setProgress(0);
        btnActionRegister.setEnabled(true);
    }

    @Override
    public void gotoMapScreen() {
        btnActionRegister.setProgress(100);
        btnActionRegister.setEnabled(true);
        TransactionManager.getInstance().gotoActivity(getActivity(), MainActivity.class, null, true);
    }

    @Override
    public void displayLocation(String location) {
        lblLocation.setText(location);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                registerClicked();
                break;
            case R.id.location:
                locationClicked();
                break;
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
        }
    }

    private void locationClicked() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(getActivity()), Constants.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void registerClicked(){
        User user = getDataFromViewsToUser();
        registerPresenter.validateData(user, txtPassword.getText().toString().trim(), txtConfirmPassword.getText().toString().trim());
    }

    private User getDataFromViewsToUser(){
        User user = new User();
        user.setEmail(txtEmail.getText().toString().trim());
        user.setName(txtName.getText().toString().trim());
        user.setFacebookLink(txtFacebook.getText().toString().trim());
        if(place != null){
            user.setLocationName(lblLocation.getText().toString().trim());
            GeoPoint geoPoint = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
            user.setlocation(geoPoint);
        }
        user.setTwitterLink(txtTwitter.getText().toString().trim());
        user.setInstagramLink(txtInstagram.getText().toString().trim());
        user.setPhoneNumber(txtPhone.getText().toString().trim());
        user.setLanguage((String) spnLanguage.getSelectedItem());
        return user;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                place = PlacePicker.getPlace(getActivity(), data);

                String toastMsg = place.getName() + " " + place.getAddress();
                displayLocation(toastMsg);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if(googleApiClient != null && googleApiClient.isConnected()){
            googleApiClient.stopAutoManage((FragmentActivity) getActivity());
            googleApiClient.disconnect();
            googleApiClient = null;
        }
        registerPresenter.releaseResources();
        super.onDestroyView();
    }
}
