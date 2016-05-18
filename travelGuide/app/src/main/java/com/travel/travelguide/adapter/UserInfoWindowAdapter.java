package com.travel.travelguide.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
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
    private ImageView imgAvatar;
    Transformation transformation;

    public UserInfoWindowAdapter(Context context) {
        transformation = new RoundedTransformationBuilder()
                .borderColor(R.color.colorPrimary)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(true)
                .build();
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
        imgAvatar = (ImageView) view.findViewById(R.id.badge);

        bindData(marker);
        return view;
    }

    private void bindData(Marker marker){
        if(users != null){
            for(User user : users){
                if(user.getbackendlessUserId().equals(marker.getSnippet())){
                    lblUsername.setText(user.getName());
                    lblDescription.setText(user.getLocationName());
//                    ImageLoader.getInstance().displayImage(user.getAvatar(), imgAvatar);
                    if(!TextUtils.isEmpty(user.getAvatar())){
                        Picasso.with(context).load(user.getAvatar()).transform(transformation).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                imgAvatar.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                imgAvatar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.anonymous_icon));
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                imgAvatar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.loading_icon));
                            }
                        });
                    }else{
                        Picasso.with(context).load(R.drawable.anonymous_icon).transform(transformation).into(imgAvatar);
                    }

                    break;
                }
            }
        }
    }
}
