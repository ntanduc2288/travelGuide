package com.travel.travelguide.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.backendless.geo.GeoPoint;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.michael.easydialog.EasyDialog;
import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.Ulti.Ulti;
import com.travel.travelguide.View.MultiSelectionSpinner;
import com.travel.travelguide.View.SocialPickerView;
import com.travel.travelguide.activity.MainActivity;
import com.travel.travelguide.manager.TransactionManager;
import com.travel.travelguide.presenter.register.IRegisterView;
import com.travel.travelguide.presenter.register.RegisterPresenter;
import com.travel.travelguide.presenter.register.RegisterPresenterImpl;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 4/23/16.
 */
public class RegisterFragment extends BaseFragment implements IRegisterView, View.OnClickListener, MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    @Bind(R.id.email)
    AppCompatEditText txtEmail;
    @Bind(R.id.password)
    AppCompatEditText txtPassword;
    @Bind(R.id.btnRegister)
    AppCompatButton btnActionRegister;
    @Bind(R.id.name)
    AppCompatEditText txtName;
    @Bind(R.id.confirm_password)
    AppCompatEditText txtConfirmPassword;
    @Bind(R.id.location)
    AppCompatButton lblLocation;
    @Bind(R.id.title)
    TextView lblTitle;
    @Bind(R.id.back_button)
    AppCompatButton btnBack;
    @Bind(R.id.phone)
    AppCompatEditText txtPhone;
    @Bind(R.id.language)
    MultiSelectionSpinner spnLanguage;
    @Bind(R.id.lnSocialContainer)
    LinearLayout lnSocialContainer;
    @Bind(R.id.button_add_social)
    AppCompatButton btnAddSocialLink;
    @Bind(R.id.interest)
    AppCompatEditText txtInterest;
    @Bind(R.id.separate_add_view)
    View separateAddView;
    @Bind(R.id.linearlayout_social_icon_container)
    LinearLayout lnSocialIconContainer;
    GoogleApiClient googleApiClient;
    RegisterPresenter registerPresenter;
    private Place place;
    private MaterialDialog dialog;
    private SocialPickerView socialPickerView;
    private EasyDialog easyDialog;


    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.register_fragment;
    }

    @Override
    protected void setupViews() {
        registerPresenter = new RegisterPresenterImpl(getFragmentManager(), this, this);
        btnActionRegister.setOnClickListener(this);
        lblLocation.setOnClickListener(this);
        btnAddSocialLink.setOnClickListener(this);
        lblTitle.setText(getString(R.string.create_account));
//        txtEmail.setText("user"+ Calendar.getInstance().getTimeInMillis() + "@gmail.com");
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

        String[] languages = Ulti.parseLanguage(getActivity());
//        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(getActivity(), R.layout.language_item, languages);
//        spnLanguage.setAdapter(languageAdapter);
//        spnLanguage.setSelection(languageAdapter.getPosition(Constants.DEFAULT_LANGUAGE));
        spnLanguage.setItems(languages);
//        spnLanguage.setSelection(28);
        spnLanguage.setSelection(new String[]{Constants.DEFAULT_LANGUAGE});
        spnLanguage.setListener(this);

        initSocialPicker();
    }

    @Override
    public void showLoading() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(getActivity())
                    .content(R.string.loading_three_dot)
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }

        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void invalidEmail() {
        txtEmail.setError(getString(R.string.invalid_email));
        btnActionRegister.setEnabled(true);
    }

    @Override
    public void invalidPassword() {
        txtPassword.setError(getString(R.string.invalid_password));
        btnActionRegister.setEnabled(true);
    }

    @Override
    public void invalidLocation() {
        btnActionRegister.setEnabled(true);
        Toast.makeText(getActivity(), getString(R.string.please_set_you_location), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String error) {
        if (!getActivity().isFinishing()) {
            Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void gotoMapScreen() {
        btnActionRegister.setEnabled(true);
        TransactionManager.getInstance().gotoActivity(getActivity(), MainActivity.class, null, true);
    }

    @Override
    public void displayLocation(String location) {
        lblLocation.setText(location);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                registerClicked();
                break;
            case R.id.location:
                locationClicked();
                break;
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
            case R.id.button_add_social:
                btnSocialLinkClicked();
                break;
        }
    }

    private void btnSocialLinkClicked() {
        if (registerPresenter.getListSocialsRemainingItems().size() > 0) {
            initSocialPicker();
            int[] attachedViewLocation = new int[2];
            btnAddSocialLink.getLocationInWindow(attachedViewLocation);
            attachedViewLocation[0] = attachedViewLocation[0] - 50;
            easyDialog = new EasyDialog(getActivity()).setLayout(socialPickerView)
                    .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent))
                    .setGravity(EasyDialog.GRAVITY_TOP)
                    .setLocationByAttachedView(btnAddSocialLink)
                    .setTouchOutsideDismiss(true)
                    .setMatchParent(false);
            easyDialog.show();
        }

    }

    public void initSocialPicker() {
        socialPickerView = new SocialPickerView(getActivity(), registerPresenter.getListSocialsRemainingItems(), socialObject -> {
            registerPresenter.getSocialInfo(socialObject);
            if (easyDialog != null) {
                easyDialog.dismiss();
            }
        });

        lnSocialIconContainer.removeAllViews();
        lnSocialIconContainer.addView(socialPickerView);
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

    private void registerClicked() {
        User user = getDataFromViewsToUser();
        registerPresenter.validateData(user, txtPassword.getText().toString().trim(), txtConfirmPassword.getText().toString().trim());
    }

    private User getDataFromViewsToUser() {
        User user = new User();
        user.setEmail(txtEmail.getText().toString().trim());
        user.setName(txtName.getText().toString().trim());
        if (place != null) {
            user.setLocationName(lblLocation.getText().toString().trim());
            GeoPoint geoPoint = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
            user.setlocation(geoPoint);
        }
        user.setLanguage(spnLanguage.getSelectedItemsAsString());
        user.setPhoneNumber(txtPhone.getText().toString().trim());
        user.setInterest(txtInterest.getText().toString().trim());


        for (SocialObject socialObject : registerPresenter.getListSocialsSelectedItems(lnSocialContainer)) {
            String socialLink = socialObject.getName();
            switch (socialObject.getId()) {
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

        return user;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK && data != null) {
                place = PlacePicker.getPlace(getActivity(), data);

                String toastMsg = place.getName() + " " + place.getAddress();
                displayLocation(toastMsg);
            }
        }

    }

    @Override
    public void onDestroyView() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage((FragmentActivity) getActivity());
            googleApiClient.disconnect();
            googleApiClient = null;
        }
        registerPresenter.releaseResources();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }

    @Override
    public void showAddSocialIconView() {
        lnSocialIconContainer.setVisibility(View.VISIBLE);
        btnAddSocialLink.setVisibility(View.GONE);
        separateAddView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddSocialIconView() {
        lnSocialIconContainer.setVisibility(View.GONE);
        separateAddView.setVisibility(View.GONE);
        btnAddSocialLink.setVisibility(View.GONE);
    }

    @Override
    public LinearLayout getLayoutSocialContainer() {
        return lnSocialContainer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
