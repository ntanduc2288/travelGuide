package com.travel.travelguide.fragment.dialog;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.manager.UserManager;
import com.travel.travelguide.presenter.rating.RatingPresenter;
import com.travel.travelguide.presenter.rating.RatingPresenterImpl;
import com.travel.travelguide.services.backendless.BackendlessController;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 9/29/16
 */
public class RatingDialogFragment extends BaseDialogFragment implements RatingPresenter.View {
    @Bind(R.id.avatar)
    CircleImageView imgAvatar;
    @Bind(R.id.lblUserName)
    AppCompatTextView lblUserName;
    @Bind(R.id.lblUserLocation)
    AppCompatTextView lblUserLocation;
    @Bind(R.id.lnUserInfo)
    LinearLayout lnUserInfo;
    @Bind(R.id.lblThankForRating)
    AppCompatTextView lblThankForRating;
    @Bind(R.id.lblPleaseRateYourHost)
    AppCompatTextView lblPleaseRateYourHost;
    @Bind(R.id.btnAddComment)
    AppCompatButton btnAddComment;
    @Bind(R.id.btnSubmit)
    AppCompatButton btnSubmit;
    @Bind(R.id.lnRatingContainer)
    LinearLayout lnRatingContainer;
    @Bind(R.id.rtbUser)
    AppCompatRatingBar rtbUser;
    @Bind(R.id.btnClose)
    AppCompatButton btnClose;

    private User user;
    private RatingPresenter.Presenter presenter;
    private MaterialDialog dialog;
    public interface RatingListener{
        void addCommentClicked();
    }

    private RatingListener ratingListener;

    public static RatingDialogFragment newInstance(Bundle bundle, User user, RatingListener ratingListener) {
        RatingDialogFragment fragment = new RatingDialogFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragment.setUser(user);
        fragment.setRatingListener(ratingListener);

        return fragment;
    }

    public void setRatingListener(RatingListener ratingListener) {
        this.ratingListener = ratingListener;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.rating_view;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        presenter = new RatingPresenterImpl(getActivity(), this, new BackendlessController());
        disableSubmitButton();
        rtbUser.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> presenter.sendRatingChangeSignal(rating));
        bindUserInfo(user);
        showSubmitMode();
        hideThankMode();
    }

    @Override
    public void enableSubmitButton() {
        btnSubmit.setEnabled(true);
    }

    @Override
    public void disableSubmitButton() {
        btnSubmit.setEnabled(false);
    }

    @Override
    public void showSubmitMode() {
        lnUserInfo.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
        lblPleaseRateYourHost.setVisibility(View.VISIBLE);
        rtbUser.setIsIndicator(false);
    }

    @Override
    public void hideSubmitMode() {
        lnUserInfo.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        lblPleaseRateYourHost.setVisibility(View.GONE);
    }

    @Override
    public void showThankMode() {
        rtbUser.setIsIndicator(true);
        lblThankForRating.setVisibility(View.VISIBLE);
        btnClose.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideThankMode() {
        lblThankForRating.setVisibility(View.GONE);
        btnClose.setVisibility(View.GONE);
    }

    @Override
    public boolean shouldHideDialogTitle() {
        return false;
    }

    @Override
    public void showLoading() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(getActivity())
                    .content(R.string.loading_three_dot)
                    .progress(true, 0)
                    .build();
        }

        if (dialog.isShowing()) {
            return;
        }

        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bindUserInfo(User user) {
        lblUserName.setText(user.getName());
        ImageLoader.getInstance().displayImage(user.getAvatar(), imgAvatar);
        lblUserLocation.setText(user.getLocationName());
    }


    @OnClick(R.id.btnAddComment)
    @Override
    public void clickedOnAddCommentButton() {
        if(ratingListener != null){
            ratingListener.addCommentClicked();
        }
        dismissDialog();
    }

    @OnClick(R.id.btnSubmit)
    @Override
    public void clickedOnSubmitButton() {
        String fromUserId = UserManager.getInstance().getCurrentUser().getbackendlessUserId();
        String toUserId = user.getbackendlessUserId();
        float ratingNumber = rtbUser.getRating();
        presenter.sendSubmitRatingSignal(fromUserId, toUserId, ratingNumber);
    }

    @OnClick(R.id.btnClose)
    @Override
    public void clickedOnCloseButton() {
        dismissDialog();
    }

    @Override
    public void dismissDialog() {
        dismiss();
    }

    @Override
    public void onDestroyView() {
        presenter.destroy();
        super.onDestroyView();
    }
}
