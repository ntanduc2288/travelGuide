package com.travel.travelguide.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.travel.travelguide.Object.SocialObject;
import com.travel.travelguide.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 5/15/16.
 */
public class SocialPickerView extends LinearLayout {

    @Bind(R.id.container)
    LinearLayout lnContainer;

    ArrayList<SocialObject> socialObjects;
    public interface SelectedSocialCallback{
        void itemSelected(SocialObject socialObject);
    }
    SelectedSocialCallback selectedSocialCallback;

    public SocialPickerView(Context context, ArrayList<SocialObject> socialObjects, SelectedSocialCallback selectedSocialCallback) {
        super(context);
        this.selectedSocialCallback = selectedSocialCallback;

        this.socialObjects = socialObjects;
        setupViews();
    }

    private void setupViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout container = (LinearLayout) inflater.inflate(R.layout.social_picker, this, true);
        ButterKnife.bind(this);

        lnContainer.removeAllViews();
        for (final SocialObject socialObject : socialObjects) {
            SocialPickerItem socialPickerItem = new SocialPickerItem(getContext(), socialObject);
            socialPickerItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedSocialCallback.itemSelected(socialObject);
                }
            });
            lnContainer.addView(socialPickerItem);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);

    }
}
