package com.travel.travelguide.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
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
public class UserInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;
    ArrayList<User> users;
    private TextView lblUsername, lblDescription;
    private AppCompatImageView imgAvatar;

    public UserInfoWindowAdapter(Context context) {
        this.context = context;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.user_info_window, null);
        lblUsername = (TextView) view.findViewById(R.id.title);
        lblDescription = (TextView) view.findViewById(R.id.snippet);
        imgAvatar = (AppCompatImageView) view.findViewById(R.id.badge);

        bindData(marker);
        return view;
    }

    private void bindData(Marker marker){
        if(users != null){
            for(User user : users){
                if(user.getId().equals(marker.getSnippet())){
                    lblUsername.setText(user.getName());
                    lblDescription.setText(user.getLocationName());
                    break;
                }
            }
        }
    }
}
