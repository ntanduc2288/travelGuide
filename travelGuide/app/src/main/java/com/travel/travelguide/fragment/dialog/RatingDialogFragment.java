package com.travel.travelguide.fragment.dialog;

import android.os.Bundle;

import com.travel.travelguide.R;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 9/29/16
 */
public class RatingDialogFragment extends BaseDialogFragment {

    public static RatingDialogFragment newInstance(Bundle bundle){
        RatingDialogFragment fragment = new RatingDialogFragment();
        if(bundle != null){
            fragment.setArguments(bundle);
        }

        return fragment;
    }
    @Override
    public int getLayoutResource() {
        return R.layout.rating_view;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

    }

    @Override
    public boolean shouldHideDialogTitle() {
        return false;
    }
}
