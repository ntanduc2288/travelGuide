package com.travel.travelguide.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.Ulti.LogUtils;
import com.travel.travelguide.adapter.UserInfoWindowAdapter;
import com.travel.travelguide.manager.TransactionManager;
import com.travel.travelguide.manager.UserManager;
import com.travel.travelguide.presenter.MapGuide.IMapGuideView;
import com.travel.travelguide.presenter.MapGuide.MapGuidePresenter;
import com.travel.travelguide.presenter.MapGuide.MapGuidePresneterImpl;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by user on 4/23/16.
 */
public class MapGuideFragment extends BaseFragment implements OnMapReadyCallback, IMapGuideView, PlaceSelectionListener, View.OnClickListener {
    private String TAG = MapGuideFragment.class.getSimpleName();
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private MapGuidePresenter mapGuidePresenter;
    private UserInfoWindowAdapter customInfoWindowAdapter;
    @Bind(R.id.loading_progress)
    ProgressBar pbLoading;
    @Bind(R.id.my_profile_avatar)
    CircularImageView imgMyProfileAvatar;

    public static MapGuideFragment newInstance() {
        return new MapGuideFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.map_guide_fragment;
    }

    @Override
    protected void setupViews() {
        //add map view
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        TransactionManager.getInstance().addFragment(getChildFragmentManager(), supportMapFragment, R.id.map_container);
        supportMapFragment.getMapAsync(this);

        mapGuidePresenter = new MapGuidePresneterImpl(getActivity(), this);
        mapGuidePresenter.checkPlayServices();

        //Init search place fragment
        SupportPlaceAutocompleteFragment placeAutocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        placeAutocompleteFragment.setHint(getString(R.string.search_a_location));
        placeAutocompleteFragment.setOnPlaceSelectedListener(this);

        bindMyProfileData(UserManager.getInstance().getCurrentUser(getActivity().getApplicationContext()));
        imgMyProfileAvatar.setOnClickListener(this);

        mapGuidePresenter.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        customInfoWindowAdapter = new UserInfoWindowAdapter(getActivity());
        mMap.setInfoWindowAdapter(customInfoWindowAdapter);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mMap.getProjection().getVisibleRegion();
                LogUtils.logD(TAG, "Zoom level: " + cameraPosition.zoom);
                mapGuidePresenter.cameraChanged(mMap);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                mapGuidePresenter.getProfileUserInfoFromUser(marker);
            }
        });

    }

    @Override
    public void onDestroyView() {
        mapGuidePresenter.disConnect();
        mapGuidePresenter.releaseResources();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapGuidePresenter.startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapGuidePresenter.stopLocationUpdates();
    }

    @Override
    public void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void displayLocation(Location lastLocation) {
        Toast.makeText(getActivity(), "Location: " + lastLocation.getLatitude() + " - " + lastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        // Add a marker in Sydney and move the camera
        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(lastLocation.getProvider()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public ArrayList<Marker> displayMarkers(ArrayList<User> users) {
        mMap.clear();
        customInfoWindowAdapter.setUsers(users);
        ArrayList<Marker> markers = new ArrayList<>();
        for (User user : users) {
            LatLng sydney = new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title(user.getLocationName())
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)));
            marker.setSnippet(user.getId());
            markers.add(marker);
        }
        return markers;
    }

    @Override
    public void zoomToLevel(CameraUpdate cameraUpdate) {
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void zoomToLevel(float level) {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(level));
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place: " + place.getName());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), Constants.DEFAULT_ZOOM_LEVEL);
        zoomToLevel(cameraUpdate);
    }

    @Override
    public void onError(Status status) {
        // TODO: Handle the error.
        Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bindMyProfileData(User user) {
        ImageLoader.getInstance().displayImage(user.getAvatar(), imgMyProfileAvatar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_profile_avatar:
                gotoProfileScreen(UserManager.getInstance().getCurrentUser(getActivity().getApplicationContext()));
                break;
        }
    }

    @Override
    public void gotoProfileScreen(User user) {
        TransactionManager.getInstance().addFragment(getFragmentManager(), ProfileFragment.newInstance(user));
//        TransactionManager.getInstance().addFragment(getFragmentManager(), RegisterFragment.newInstance());
    }
}
