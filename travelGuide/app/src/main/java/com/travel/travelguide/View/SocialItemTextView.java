package com.travel.travelguide.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 5/14/16.
 */
public class SocialItemTextView extends LinearLayout{

    @Bind(R.id.social)
    AppCompatTextView lblSocial;

    SocialObject socialObject;

    public SocialItemTextView(Context context, SocialObject socialObject) {
        super(context);
        this.socialObject = socialObject;
        setupViews();
    }



    public SocialObject getSocialObject()
    {
        socialObject.setName(lblSocial.getText().toString().trim());
        return socialObject;
    }

    private void setupViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View container = layoutInflater.inflate(R.layout.social_item_textview, this, true);
        ButterKnife.bind(this);
        mappingSocialType();
    }

    private void mappingSocialType() {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.facebook, null);
        String hint = Constants.EMPTY_STRING;
        switch (socialObject.getId()){
            case SocialObject.FACEBOOK_TYPE:
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.facebook, null);
                hint = getContext().getString(R.string.input_facebook_link);
                break;
            case SocialObject.TWITTER_TYPE:
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.twitter, null);
                hint = getContext().getString(R.string.input_twitter_link);
                break;
            case SocialObject.INSTAGRAM_TYPE:
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.instagram, null);
                hint = getContext().getString(R.string.input_instagram_link);
                break;
        }

        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();
        drawable.setBounds(0,0,w,h);

        lblSocial.setCompoundDrawables(drawable, null, null, null);
        lblSocial.setHint(hint);
        lblSocial.setText(socialObject.getName());
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }
}
