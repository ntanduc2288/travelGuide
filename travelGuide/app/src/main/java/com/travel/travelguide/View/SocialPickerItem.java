package com.travel.travelguide.View;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 5/15/16.
 */
public class SocialPickerItem extends LinearLayout {
    @Bind(R.id.textview_social_name)
    AppCompatTextView lblSocialName;
    @Bind(R.id.imageview)
    AppCompatImageView imgSocial;
    SocialObject socialObject;
    public SocialPickerItem(Context context, SocialObject socialObject) {
        super(context);
        this.socialObject = socialObject;
        setupView();
    }

    private void setupView() {
       LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View inflate = layoutInflater.inflate(R.layout.textview_item, this, true);
        ButterKnife.bind(this);

        switch (socialObject.getId()){
            case SocialObject.FACEBOOK_TYPE:
//                lblSocialName.setText(R.string.add_facebook_link);
                imgSocial.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.facebook_icon));
                break;
            case SocialObject.INSTAGRAM_TYPE:
//                lblSocialName.setText(R.string.add_instagram_link);
                imgSocial.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.instagram_icon));
                break;
            case SocialObject.TWITTER_TYPE:
//                lblSocialName.setText(R.string.add_twitter_link);
                imgSocial.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.twitter_icon));
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }
}
