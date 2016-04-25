package com.travel.travelguide.presenter.MapGuide;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.Ulti.LogUtils;
import com.travel.travelguide.manager.UserManager;

import java.util.ArrayList;

/**
 * Created by user on 4/24/16.
 */
public class MapGuidePresneterImpl implements MapGuidePresenter, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    IMapGuideView mapGuideView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private Activity activity;
    private Location mLastLocation;
    private String TAG = MapGuidePresneterImpl.class.getSimpleName();

    private String USERS_RADIUS_WHERE_CLAUSE = "distance(%s, %s, locations.latitude, locations.longitude)" + " <= mi(%s)";

    public MapGuidePresneterImpl(Activity activity, IMapGuideView mapGuideView) {
        this.mapGuideView = mapGuideView;
        this.activity = activity;
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
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (viewIsValid()) {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                mapGuideView.displayLocation(mLastLocation);
                getUserList(mLastLocation);
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
                getUserList(mLastLocation);
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
                    mapGuideView.showError(fault.getMessage());
                }
            }
        });
    }

    @Override
    public void getUserListWithRadius(float radius) {
        User currentUser = UserManager.getInstance().getUser();
        String whereClause = String.format(USERS_RADIUS_WHERE_CLAUSE, currentUser.getLocation().getLatitude(), currentUser.getLocation().getLongitude(), radius);
        LogUtils.logD(TAG, "Where clause: " + whereClause);
        BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery(whereClause);
        Backendless.Persistence.of(BackendlessUser.class).find(backendlessDataQuery, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                mapBackendlessUserToUser(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (viewIsValid()) {
                    mapGuideView.showError(fault.getMessage());
                }
            }
        });
    }

    private void mapBackendlessUserToUser(BackendlessCollection<BackendlessUser> response){
        LogUtils.logD(TAG, "get user data response: " + response.toString());


        ArrayList<User> users = new ArrayList<User>();
        ArrayList<Marker> markers = new ArrayList<Marker>();
        for (BackendlessUser backendlessUser : response.getData()) {
            User user = new User(backendlessUser);
            users.add(user);
        }

        if (viewIsValid()) {
            markers.addAll(mapGuideView.displayMarkers(users));
            mapGuideView.zoomBound(markers);
        }
    }

    @Override
    public boolean viewIsValid() {
        if (mapGuideView != null) return true;
        return false;
    }

    @Override
    public void releaseResources() {
        mapGuideView = null;
    }
}
