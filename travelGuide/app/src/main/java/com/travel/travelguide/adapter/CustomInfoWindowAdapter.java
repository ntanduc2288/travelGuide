package com.travel.travelguide.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;

import java.util.ArrayList;

/**
 * Created by user on 4/26/16.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;
    ArrayList<User> users;
    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.custom_info_window, null);
        if(users != null){
            TextView lblTitle = (TextView) view.findViewById(R.id.title);
            for(User user : users){
                if(user.getId().equals(marker.getSnippet())){
                    lblTitle.setText(user.getLocationName());
                    break;
                }
            }
        }
        return view;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        return null;
    }
}
