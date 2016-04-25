package com.travel.travelguide.fragment;

import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.manager.TransactionManager;
import com.travel.travelguide.presenter.MapGuide.IMapGuideView;
import com.travel.travelguide.presenter.MapGuide.MapGuidePresenter;
import com.travel.travelguide.presenter.MapGuide.MapGuidePresneterImpl;

import java.util.ArrayList;

/**
 * Created by user on 4/23/16.
 */
public class MapGuideFragment extends BaseFragment implements OnMapReadyCallback, IMapGuideView{
    private String TAG = MapGuideFragment.class.getSimpleName();

    private GoogleMap mMap;
    private MapGuidePresenter mapGuidePresenter;

    public static MapGuideFragment newInstance() {
        return new MapGuideFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.map_guide_fragment;
    }

    @Override
    protected void setupViews() {
        MapFragment mapFragment = MapFragment.newInstance();
        TransactionManager.getInstance().addFragment(getFragmentManager(), mapFragment, R.id.map_container);
        mapFragment.getMapAsync(this);

        mapGuidePresenter = new MapGuidePresneterImpl(getActivity(),this);
        mapGuidePresenter.checkPlayServices();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



    }

    @Override
    public void onDestroyView() {
        mapGuidePresenter.releaseResources();
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapGuidePresenter.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapGuidePresenter.startLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapGuidePresenter.disConnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapGuidePresenter.stopLocationUpdates();
    }



    @Override
    public void displayLocation(Location lastLocation) {
        Toast.makeText(getActivity(), "Location: " + lastLocation.getLatitude() + " - " + lastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title(lastLocation.getProvider()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public ArrayList<Marker> displayMarkers(ArrayList<User> users) {
        mMap.clear();
        ArrayList<Marker> markers = new ArrayList<>();
        for(User user : users){
            LatLng sydney = new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title(user.getLocationName()));
            markers.add(marker);
        }
        return markers;
    }

    @Override
    public void zoomBound(ArrayList<Marker> markers) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker m : markers) {
            builder.include(m.getPosition());
        }

        LatLngBounds bounds = builder.build();
        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}
