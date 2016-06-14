package com.travel.travelguide.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.travel.travelguide.Object.Preview;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.adapter.ReviewAdapter;
import com.travel.travelguide.presenter.Preview.PreviewPresenter;
import com.travel.travelguide.presenter.Preview.PreviewPresenterImpl;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by user on 5/19/16.
 */
public class ReviewFragment extends BaseFragment implements View.OnClickListener, PreviewPresenter.PreviewView {
    @Bind(R.id.recyclerview)
    RecyclerView rcReview;
    @Bind(R.id.edittext_review)
    AppCompatEditText txtPreview;
    @Bind(R.id.button_send)
    AppCompatButton btnSend;
    @Bind(R.id.progressbar_loading)
    ProgressBar prbLoading;

    ReviewAdapter reviewAdapter;
    PreviewPresenter.Presenter presenter;
    MaterialDialog dialog;

    User user;
    private LinearLayoutManager linearLayoutManager;

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
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcReview.setLayoutManager(linearLayoutManager);
        rcReview.setAdapter(reviewAdapter);
        btnSend.setOnClickListener(this);
        presenter = new PreviewPresenterImpl(this, user);

    }

    public void checkToLoadData(){
        if(reviewAdapter.getItemCount() == 0)
            presenter.getPreviewList(user.getbackendlessUserId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_send:
                presenter.sendPreview(txtPreview.getText().toString().trim());
                break;
        }
    }

    @Override
    public void showLoading() {
        if(dialog == null){
            dialog = new MaterialDialog.Builder(getActivity())
                    .content(R.string.waiting_dot)
                    .backgroundColor(getResources().getColor(R.color.transparent))
                    .progress(true, 0)
                    .build();
        }

        if(!dialog.isShowing()){
            dialog.show();
        }

    }

    @Override
    public void hideLoading() {
        if(dialog != null){
            dialog.dismiss();
        }
    }

    @Override
    public void showProgressbar() {
        prbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        prbLoading.setVisibility(View.GONE);
    }

    @Override
    public void bindPreviewContentOnView(Preview preview) {
        txtPreview.setText("");
        reviewAdapter.addPreview(preview);
        linearLayoutManager.scrollToPositionWithOffset(reviewAdapter.getItemCount(), 0);
    }

    @Override
    public void bindPreviewsOnView(ArrayList<Preview> previews) {
        reviewAdapter.setPreviews(previews);
        linearLayoutManager.scrollToPositionWithOffset(reviewAdapter.getItemCount(), 0);
    }

    @Override
    public void showAler(String alertMessage) {
        Toast.makeText(getActivity(), "Error: " + alertMessage, Toast.LENGTH_SHORT).show();
    }
}
