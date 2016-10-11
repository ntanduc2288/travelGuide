package com.travel.travelguide.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.MobiComConversationFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.otto.Subscribe;
import com.travel.travelguide.Bus.EvenBusHelper;
import com.travel.travelguide.Bus.object.RatingChangedBusObject;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.Ulti;
import com.travel.travelguide.adapter.UserProfileAdapter;
import com.travel.travelguide.fragment.dialog.RatingDialogFragment;
import com.travel.travelguide.presenter.userProfile.UserProfilePresenter;
import com.travel.travelguide.presenter.userProfile.UserProfilePresenterImpl;
import com.travel.travelguide.services.backendless.BackendlessController;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 5/18/16.
 */
public class UserProfileFragment extends BaseFragment implements UserProfilePresenter.View, View.OnClickListener {
    @Bind(R.id.title)
    AppCompatTextView lblTitle;
    @Bind(R.id.back_button)
    AppCompatButton btnBack;
    @Bind(R.id.avatar)
    CircleImageView imgAvatar;
    @Bind(R.id.btn_about)
    AppCompatButton btnAbout;
    @Bind(R.id.btn_reviews)
    AppCompatButton btnReviews;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton btnChat;
    @Bind(R.id.layout_menu)
    LinearLayout lnTabBar;
    @Bind(R.id.viewpager)
    ViewPager viewPagerProfile;
    @Bind(R.id.rtbUser)
    AppCompatRatingBar rtbUser;
    UserProfileAdapter userProfileAdapter;
    AboutFragment aboutFragment;
    ReviewFragment reviewFragment;

    MaterialDialog dialog;
    User user;
    UserProfilePresenter.Presenter presenter;
    private final int TAB_ABOUT = 0;
    private final int TAB_REVIEW = 1;

    private int currentTab = TAB_ABOUT;

    public static UserProfileFragment newInstance(User user) {
        UserProfileFragment profileFragment = new UserProfileFragment();
        profileFragment.setUser(user);
        return profileFragment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_profile_screen;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new UserProfilePresenterImpl(this, new BackendlessController());
        userProfileAdapter = new UserProfileAdapter(getChildFragmentManager());
        aboutFragment = AboutFragment.newInstance(user);
        reviewFragment = ReviewFragment.newInstance(user);
        userProfileAdapter.addFragment(aboutFragment);
        userProfileAdapter.addFragment(reviewFragment);
    }

    @Override
    protected void setupViews() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.black_transparent));
        btnBack.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnReviews.setOnClickListener(this);

        viewPagerProfile.setAdapter(userProfileAdapter);
        viewPagerProfile.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateCurrentTab(position);
                if (position == TAB_REVIEW) {
                    reviewFragment.checkToLoadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        selectAboutTab();
        bindData(user);

        rtbUser.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                openRatingDialog();
            }
            return true;
        });

        presenter.getAverageRatingNumber(user.getbackendlessUserId());

    }

    @Override
    public void showLoading() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(getActivity())
                    .content(getString(R.string.loading_three_dot))
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }

        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void bindData(User user) {
        lblTitle.setText(user.getName());
        ImageLoader.getInstance().displayImage(user.getAvatar(), imgAvatar);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
            case R.id.fab:
                gotoConversationActivity();
                break;
            case R.id.btn_about:
                selectAboutTab();
                break;
            case R.id.btn_reviews:
                selectReviewsTab();
                break;
        }
    }

    @Override
    public void gotoConversationActivity() {
        Intent intent = new Intent(getActivity(), ConversationActivity.class);
        intent.putExtra(ConversationUIService.USER_ID, user.getbackendlessUserId());
        intent.putExtra(ConversationUIService.DISPLAY_NAME, user.getName()); //put it for displaying the title.

        intent.putExtra(MobiComConversationFragment.DEFAULT_CONTENT, Ulti.getDefaultMessageContent());
        intent.putExtra(ConversationActivity.TAKE_ORDER, true);
        startActivity(intent);
    }

    private void selectReviewsTab() {
        updateCurrentTab(TAB_REVIEW);

    }

    private void selectAboutTab() {
        updateCurrentTab(TAB_ABOUT);
    }

    @Override
    public void updateCurrentTab(int currentTab) {
        this.currentTab = currentTab;
        for (int i = 0; i < lnTabBar.getChildCount(); i++) {
            View childView = lnTabBar.getChildAt(i);
            if (childView instanceof AppCompatButton) {
                childView.setSelected(false);
                childView.setEnabled(true);
                ((AppCompatButton) childView).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                if (i == 0) {
                    childView.setBackgroundResource(R.drawable.xml_left_tab_find_people);
                } else if (i == lnTabBar.getChildCount() - 1) {
                    boolean flag = true;
                    int position = i;
                    View rightSideView = lnTabBar.getChildAt(position);
                    while (flag) {
                        if (rightSideView.getVisibility() == View.VISIBLE) {
                            rightSideView.setBackgroundResource(R.drawable.xml_right_tab_find_people);
                            flag = false;
                        } else {
                            position--;
                            if (position > 0) {
                                rightSideView = lnTabBar.getChildAt(position);
                            } else {
                                flag = false;
                            }
                        }
                    }
                } else {
                    childView.setBackgroundResource(R.drawable.xml_midle_tab);
                }
            }


        }

        switch (currentTab) {
            case TAB_ABOUT:
                btnAbout.setSelected(true);
                btnAbout.setEnabled(false);
                btnAbout.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
                break;
            case TAB_REVIEW:
                btnReviews.setSelected(true);
                btnReviews.setEnabled(false);
                btnReviews.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
                break;
            default:
                break;
        }

        viewPagerProfile.setCurrentItem(currentTab);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EvenBusHelper.getInstance().registerEventBus(this);
    }

    @Override
    public void onDestroyView() {
        viewPagerProfile.removeAllViews();
        EvenBusHelper.getInstance().unRegisterEventBus(this);
        presenter.destroy();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void openRatingDialog() {
        RatingDialogFragment ratingDialogFragment = RatingDialogFragment.newInstance(null, user, new RatingDialogFragment.RatingListener() {
            @Override
            public void addCommentClicked() {
                selectReviewsTab();
            }
        });
        ratingDialogFragment.show(getChildFragmentManager(), ratingDialogFragment.getTag());
    }

    @Override
    public void bindRatingNumber(float ratingNumber) {
        rtbUser.setRating(ratingNumber);
    }

    @Subscribe
    @Override
    public void receivedRatingChangedSignal(RatingChangedBusObject ratingChangedBusObject) {
        Log.d("UserProfileFragment", "ratingEntityObject.getRatingNumber():" + ratingChangedBusObject.getAverageNumber());
        presenter.receivedRatingNumber(user, ratingChangedBusObject);
    }
}
