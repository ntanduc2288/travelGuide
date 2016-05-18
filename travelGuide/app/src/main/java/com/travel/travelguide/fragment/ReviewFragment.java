package com.travel.travelguide.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.adapter.ReviewAdapter;

import butterknife.Bind;

/**
 * Created by user on 5/19/16.
 */
public class ReviewFragment extends BaseFragment {
    @Bind(R.id.recyclerview)
    RecyclerView rcReview;
    ReviewAdapter reviewAdapter;

    User user;
    public static ReviewFragment newInstance(User user){
        ReviewFragment reviewFragment = new ReviewFragment();
        reviewFragment.setUser(user);
        return reviewFragment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewAdapter = new ReviewAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.review_view;
    }

    @Override
    protected void setupViews() {

        rcReview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcReview.setAdapter(reviewAdapter);
    }

    public void checkToLoadData(){

    }
}
