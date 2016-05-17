package com.travel.travelguide.presenter.MapGuide;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.Ulti.LogUtils;
import com.travel.travelguide.Ulti.MapUlti;
import com.travel.travelguide.manager.UserManager;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by user on 4/24/16.
 */
public class MapGuidePresneterImpl implements MapGuidePresenter, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private final CompositeSubscription compositeSubscription;

    IMapGuideView mapGuideView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    // Location updates intervals in sec
    private final int UPDATE_INTERVAL = 10000; // 10 sec
    private final int FATEST_INTERVAL = 5000; // 5 sec
    private final int DISPLACEMENT = 10; // 10 meters
    private final int DELAY_TIME = 1500; // millisecond - after this time, get users data from server

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private Activity activity;
    private Location mLastLocation;
    private Location previousCenterLocation;
    private float previousRadius = 0;
    private String TAG = MapGuidePresneterImpl.class.getSimpleName();
    private String USERS_RADIUS_WHERE_CLAUSE = "distance(%s, %s, locations.latitude, locations.longitude)" + " <= km(%s)";
    private Handler handler;
    ArrayList<User> users;

    public MapGuidePresneterImpl(Activity activity, IMapGuideView mapGuideView) {
        this.mapGuideView = mapGuideView;
        this.activity = activity;
        handler = new Handler();
        users = new ArrayList<User>();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(activity.getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                activity.finish();
            }
            if (viewIsValid()) {
                mapGuideView.showError("This device is not supported.");
            }
        } else {

            buildGoogleApiClient();
            createLocationRequest();
        }
    }

    @Override
    public void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    @Override
    public void connect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void disConnect() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void startLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (viewIsValid()) {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, Constants.DEFAULT_ZOOM_LEVEL);
                mapGuideView.zoomToLevel(cameraUpdate);
            } else {
                mapGuideView.showError("(Couldn't get the location. Make sure location is enabled on the device)");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (viewIsValid()) {
            // Assign the new location
            mLastLocation = location;

            if (mLastLocation != null) {
                mapGuideView.displayLocation(mLastLocation);
                getUserListWithRadius(mLastLocation.getLatitude(), mLastLocation.getLongitude(), Constants.DEFAULT_RADIUS);
            } else {
                mapGuideView.showError("(Couldn't get the location. Make sure location is enabled on the device)");
            }
        }
    }

    @Override
    public void getUserList(Location location) {
        BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery();
        Backendless.Persistence.of(BackendlessUser.class).find(backendlessDataQuery, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                mapBackendlessUserToUser(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (viewIsValid()) {
                    mapGuideView.hideLoadindMarkerProcess();
                    mapGuideView.showError(fault.getMessage());
                }
            }
        });
    }

    @Override
    public void getUserListWithRadius(double latitude, double longitude, float radius) {
        mapGuideView.showLoadingMarkerProcess();
        String whereClause = String.format(USERS_RADIUS_WHERE_CLAUSE, latitude, longitude, radius);
        LogUtils.logD(TAG, "Where clause: " + whereClause);
        BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery(whereClause);
        Backendless.Persistence.of(BackendlessUser.class).find(backendlessDataQuery, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                if (viewIsValid()) {
                    mapGuideView.hideLoadindMarkerProcess();
                    mapBackendlessUserToUser(response);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (viewIsValid()) {
                    mapGuideView.hideLoadindMarkerProcess();
                    mapGuideView.showError(fault.getMessage());
                }
            }
        });
    }

    //Map backendless user to normal user then update user's marker on map
    private void mapBackendlessUserToUser(BackendlessCollection<BackendlessUser> response) {
        LogUtils.logD(TAG, "get user data response: " + response.getData().toString());


        users.clear();
        ArrayList<Marker> markers = new ArrayList<Marker>();
        for (BackendlessUser backendlessUser : response.getData()) {
            User user = new User(backendlessUser);
            users.add(user);
        }

        if (viewIsValid()) {
            markers.addAll(mapGuideView.displayMarkers(users));
        }
    }

    @Override
    public void searchPlace(Fragment fragment, String query) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)

                    .build(fragment.getActivity());

            fragment.startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public boolean viewIsValid() {
        if (mapGuideView != null) return true;
        return false;
    }

    @Override
    public void cameraChanged(final GoogleMap googleMap) {
        mapGuideView.hideLoadindMarkerProcess();
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewIsValid()) {
                    mapGuideView.showLoadingMarkerProcess();
                    float radius = MapUlti.getRadius(googleMap);
                    Location centerLocation = MapUlti.getCenterLocation(googleMap);
                    boolean needToGetListWithRadius = previousCenterLocation == null || centerLocation.distanceTo(previousCenterLocation) > 10 ||
                            Math.abs(radius - previousRadius) > 1;
                    if (needToGetListWithRadius) {
                        previousRadius = radius;
                        previousCenterLocation = centerLocation;
                        getUserListWithRadius(centerLocation.getLatitude(), centerLocation.getLongitude(), radius);
                    }else {
                        mapGuideView.hideLoadindMarkerProcess();
                    }
                }

            }
        }, DELAY_TIME);
    }


    @Override
    public void releaseResources() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        mapGuideView = null;

        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getProfileUserInfoFromUser(Marker marker) {
        if (mapGuideView != null) {
            mapGuideView.showLoadingMarkerProcess();
            for (User user : users) {
                if (user.getbackendlessUserId().endsWith(marker.getSnippet())) {
                    mapGuideView.hideLoadindMarkerProcess();
                    mapGuideView.gotoProfileScreen(user);
                    break;
                }
            }

        }

    }

    @Override
    public void logout() {
        mapGuideView.showLoading();


        Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                clearLocalUserData();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (viewIsValid()) {
                    mapGuideView.hideLoading();
                    mapGuideView.showMessage(fault.getMessage());
                }
            }
        });

    }

    private void clearLocalUserData() {

        Observable observable = Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return Observable.just(UserManager.getInstance().clearCurrentUserInfo(mapGuideView.getContext().getApplicationContext()));
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        compositeSubscription.add(observable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (viewIsValid()) {
                    mapGuideView.hideLoading();
                    mapGuideView.gotoLoginScreen();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (viewIsValid()) {
                    mapGuideView.hideLoading();
                    mapGuideView.showMessage("Could not logout");
                }
            }
        }));
    }

}
