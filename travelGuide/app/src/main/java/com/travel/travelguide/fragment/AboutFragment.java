package com.travel.travelguide.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.View.SocialItemTextView;
import com.travel.travelguide.presenter.about.IAboutView;

import butterknife.Bind;

/**
 * Created by user on 5/19/16.
 */
public class AboutFragment extends BaseFragment implements IAboutView {
    @Bind(R.id.email_content)
    AppCompatTextView lblEmail;
    @Bind(R.id.phone_content)
    AppCompatTextView lblPhone;
    @Bind(R.id.language_content)
    AppCompatTextView lblLanguages;
    @Bind(R.id.social_container)
    LinearLayout lnSocialLinkContainer;
    @Bind(R.id.location_content)
    AppCompatTextView lblLocation;
    @Bind(R.id.interest_content)
    AppCompatTextView lblInterests;
    @Bind(R.id.aboutme_content)
    AppCompatTextView lblAboutMe;
    User user;

    public static AboutFragment newInstance(User user) {
        AboutFragment aboutFragment = new AboutFragment();
        aboutFragment.setUser(user);
        return aboutFragment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_info_section;
    }

    @Override
    protected void setupViews() {
        bindData(user);
    }

    @Override
    public void bindData(User user) {
        setText(lblEmail, user.getEmail());
        setText(lblLocation, user.getLocationName());
        setText(lblPhone, user.getPhoneNumber());
        setText(lblLanguages, user.getLanguage());
        setText(lblAboutMe, user.getAboutMe());


        if(!TextUtils.isEmpty(user.getFacebookLink())){
            SocialItemTextView socialItemView = new SocialItemTextView(getContext(), new SocialObject(SocialObject.FACEBOOK_TYPE, user.getFacebookLink()));
            lnSocialLinkContainer.addView(socialItemView);
        }

        if(!TextUtils.isEmpty(user.getInstagramLink())){
            SocialItemTextView socialItemView = new SocialItemTextView(getContext(), new SocialObject(SocialObject.INSTAGRAM_TYPE, user.getInstagramLink()));
            lnSocialLinkContainer.addView(socialItemView);
        }

        if(!TextUtils.isEmpty(user.getTwitterLink())){
            SocialItemTextView socialItemView = new SocialItemTextView(getContext(), new SocialObject(SocialObject.TWITTER_TYPE, user.getTwitterLink()));
            lnSocialLinkContainer.addView(socialItemView);
        }


    }

    @Override
    public void showMessage(String message) {

    }

    private void setText(TextView textView, String content) {
        if (TextUtils.isEmpty(content)) {
            textView.setText(R.string.not_available);
        } else {
            textView.setText(content);
        }
    }
}
